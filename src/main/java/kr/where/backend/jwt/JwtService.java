package kr.where.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.filter.JwtConstants;
import kr.where.backend.auth.oauth2login.cookie.CookieShop;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.redisToken.RedisTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class JwtService {
    private static final int ACCESS_EXPIRY = 30 * 60;
    @Value("${accesstoken.expiration.time}")
    private long accessTokenExpirationTime;
    @Value("${refreshtoken.expiration.time}")
    private long refreshTokenExpirationTime;
    @Value("${jwt.token.secret}")
    private String secret_code;
    @Value("${issuer}")
    private String issuer;
    private final MemberService memberService;
    private final RedisTokenService redisTokenService;
    /**
     * 쿠키에 있는 refreshToken을 사용하여 재발급
     * @param refreshToken : 재발급을 위한 refreshToken을 httpOnly로 설정되어있기 때문에 값을 받아온다.
     * @return accessToken
     */
    public ResponseAccessTokenDTO reissueAccessToken(final HttpServletResponse response,
                                                     final String refreshToken) {

        final Claims claims = parseToken(refreshToken);
        Integer memberId = (Integer) claims.get("intraId");
        String intraName = (String) claims.get("intraName");

        String savedRefreshToken = redisTokenService.getRefreshToken("refreshToken" + memberId);
        if (!refreshToken.equals(savedRefreshToken)) {
            throw new JwtException.InvalidJwtToken();
        }

        String accessToken = redisTokenService.getAccessToken("accessToken" + memberId);
        if (accessToken == null || accessToken.isEmpty()) {
            accessToken = createAccessToken(memberId, intraName);
            redisTokenService.saveAccessToken("accessToken" + memberId, accessToken);
        }

        CookieShop.bakedCookie(
                response,
                JwtConstants.ACCESS.getValue(),
                ACCESS_EXPIRY,
                accessToken,
                false
        );

        return ResponseAccessTokenDTO
                .builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * tokenProvider와 합칠 생각이여서, 가져옴
     * accessToken 만료 시간을 통해서, token 생성
     * @param intraId : token의 주인을 token payload에 저장
     * @return Token을 생성하는 메서드 호출
     */
    public String createAccessToken(final Integer intraId, final String intraName) {
        return createToken(
                intraId,
                intraName,
                accessTokenExpirationTime,
                JwtConstants.ACCESS.getValue()
        );
    }

    /**
     * tokenProvider와 합칠 생각이여서, 가져옴
     * refreshToken 만료 시간을 통해서, token 생성
     * @param intraId : token의 주인을 token payload에 저장
     * @return Token을 생성하는 메서드 호출
     */
    public String createRefreshToken(final Integer intraId, final String intraName) {
        return createToken(
                intraId,
                intraName,
                refreshTokenExpirationTime,
                JwtConstants.REFRESH.getValue()
        );
    }

    /**
     * token을 생성하는 메서드, jwt 안의 claim에 생성 시간, 생성자, 만료시간, secret Key 설정한 후 build
     * @param intraId : token 주인을 payload에 설정하기 위함
     * @param validateTime : 토큰의 만료시간 설정하기 위함
     * @return token
     */
    private String createToken(
            final Integer intraId, final String intraName, final long validateTime, final String type
        ) {
        final Claims claims = Jwts.claims().setSubject(JwtConstants.USER_SUBJECTS.getValue());
        claims.put(JwtConstants.USER_ID.getValue(), intraId);
        claims.put(JwtConstants.USER_NAME.getValue(), intraName);
        claims.put(JwtConstants.TOKEN_TYPE.getValue(), type);
        claims.put(JwtConstants.ROLE_LEVEL.getValue(), JwtConstants.USER_ROLE.getValue());
        final Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + validateTime))
                .signWith(generateSecretKey(secret_code), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * jwt Token 설정 시 필요한 secret code 복호화 메서드
     * @param secretCode : 사용자 지정 secretcode
     * @return 복호화된 code
     */
    private Key generateSecretKey(final String secretCode) {
        final String encodedSecretCode = Base64.getEncoder().encodeToString(secretCode.getBytes());
        return Keys.hmacShaKeyFor(encodedSecretCode.getBytes());
    }

    /**
     * security의 권한과 관련된 메서드
     * token을 받아, 파싱한 후 권한 정보를 찾아온 후
     * intraId 값으로 member을 찾아 권한을 부여한다
     * @param token
     * @return
     */
    public Authentication getAuthentication(final HttpServletRequest request, final String token) {
        // 토큰 복호화
        final Claims claims = parseToken(token);
        log.info("[login] : 이름 {}, 토큰 {}, 롤 {}", claims.get(
                JwtConstants.USER_NAME.getValue()),
                token,
                claims.get(JwtConstants.ROLE_LEVEL.getValue())
        );

        validateTypeAndClaims(request, claims);

        // 클레임에서 권한 정보 가져오기
        final Integer intraId = claims.get(JwtConstants.USER_ID.getValue(), Integer.class);

        //token 에 담긴 정보에 맵핑되는 User 정보 디비에서 조회
        final Member member = memberService.findOne(intraId)
                .orElseThrow(JwtException.NotFoundJwtToken::new);

        final Collection<? extends GrantedAuthority> authorities
                = Stream.of("ROLE_" + member.getRole())
                .map(SimpleGrantedAuthority::new)
                .toList();

        //Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(
                new AuthUser(member.getIntraId(),
                        member.getIntraName(),
                        member.getDefaultGroupId()),
                "",
                authorities);
    }

    /**
     * acessToken을 파싱하여, 유효한 token인지 판별
     * 1. 유효한 토큰인지
     * 2. 토큰 만료 시간이 다 되었는지
     * 3. 지원 되지 않은 토큰 인지
     * 4. 사용자 지정 유효한 토큰인지
     * @param accessToken : 판별할 token
     * @return 파싱된 claim
     */
    private Claims parseToken(final String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateSecretKey(secret_code))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new JwtException.InvalidJwtToken();
        } catch (ExpiredJwtException e) {
            throw new JwtException.ExpiredJwtToken();
        } catch (UnsupportedJwtException e) {
            throw new JwtException.UnsupportedJwtToken();
        } catch (IllegalArgumentException e) {
            throw new JwtException.IllegalJwtToken();
        } catch (SignatureException e) {
            throw new JwtException.WrongSignedJwtToken();
        }
    }

    public Optional<String> extractToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isEmpty(authorization)) {
            return Optional.empty();
        }
        return getToken(authorization.split(" "));
    }

    private Optional<String> getToken(final String[] token) {
        if (token.length != 2 || !token[0].equals(JwtConstants.HEADER_TYPE.getValue())) {
            return Optional.empty();
        }
        return Optional.ofNullable(token[1]);
    }

    private void validateTypeAndClaims(final HttpServletRequest request, final Claims claims) {
        if (!Objects.equals(request.getRequestURI(), JwtConstants.REISSUE_URI.getValue())
                && !Objects.equals(claims.get(JwtConstants.TOKEN_TYPE.getValue()), JwtConstants.ACCESS.getValue())) {
            throw new JwtException.UnMatchedTypeJwtToken();
        }

        if (Objects.equals(request.getRequestURI(), JwtConstants.REISSUE_URI.getValue())
                && !Objects.equals(claims.get(JwtConstants.TOKEN_TYPE.getValue()), JwtConstants.REFRESH.getValue())) {
            throw new JwtException.UnMatchedTypeJwtToken();
        }

        if (claims.get(JwtConstants.TOKEN_TYPE.getValue()) == null) {
            throw new JwtException.IllegalJwtToken();
        }
    }
}
