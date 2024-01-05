package kr.where.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;
import kr.where.backend.jwt.dto.ReIssue;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.ExpiredOAuthTokenTimeOutException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.IllegalOAuthTokenException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.InvalidedOAuthTokenException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.UnsupportedOAuthTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${accesstoken.expiration.time}")
    private Long accessTokenExpirationTime;
    @Value("${refreshtoken.expiration.time}")
    private Long refreshTokenExpirationTime;
    @Value("${jwt.token.secret}")
    private String secret_code;
    @Value("${issuer}")
    private String issuer;
    private final JwtRepository jwtRepository;
    private final MemberService memberService;

    /**
     * 로그인할때 발급 받는 jwt token을 저장하기 위한 메서드
     *
     * @param intraId : 카뎃의 고유 id를 같이 저장한다. relation 없이 사용하기 위한 용도
     * @param refreshToken : 발급 받은 refreshToken 저장
     */
    @Transactional
    public void create(final Integer intraId, final String refreshToken) {
        jwtRepository.save(new JsonWebToken(intraId, refreshToken));
    }

    public JsonWebToken findById(final Integer intraId) {
        return jwtRepository
                .findByIntraId(intraId)
                .orElseThrow(InvalidedOAuthTokenException::new);
    }

    /**
     * 다시 로그인을 하거나, refresh Token 까지 만료 되었을때, DB에 저장된 token을 update해주는 메서드
     * @param intraId : 개인 토큰을 찾기 위함
     * transactional을 사용하여, 변경점이 생기면 자동 저장, 영속성 context 관점
     */
    @Transactional
    public void updateJsonWebToken(final Integer intraId, final String intraName) {
        final JsonWebToken jsonWebToken = findById(intraId);

        jsonWebToken.updateRefreshToken(createRefreshToken(intraId, intraName));
    }

    /**
     * 헤더에 들어있는 accessToken의 시간이 만료되었을 떄, refreshToken을 사용하여 재발급
     * @param reIssue : client 측에서 들어온 intraId 와 refreshToken을 통해 재발급 용
     * @return accessToken
     */
    @Transactional
    public String reissueAccessToken(final ReIssue reIssue) {
        final JsonWebToken jsonWebToken = findById(reIssue.getIntraId());

        jsonWebToken.validateRefreshToken(reIssue.getRefreshToken());

        return createAccessToken(reIssue.getIntraId(), "name");
    }

    /**
     * tokenProvider와 합칠 생각이여서, 가져옴
     * accessToken 만료 시간을 통해서, token 생성
     * @param intraId : token의 주인을 token payload에 저장
     * @return Token을 생성하는 메서드 호출
     */
    public String createAccessToken(final Integer intraId, final String intraName) {
        return createToken(intraId, intraName, accessTokenExpirationTime);
    }

    /**
     * tokenProvider와 합칠 생각이여서, 가져옴
     * refreshToken 만료 시간을 통해서, token 생성
     * @param intraId : token의 주인을 token payload에 저장
     * @return Token을 생성하는 메서드 호출
     */
    public String createRefreshToken(final Integer intraId, final String intraName) {
        return createToken(intraId, intraName, refreshTokenExpirationTime);
    }

    /**
     * token을 생성하는 메서드, jwt 안의 claim에 생성 시간, 생성자, 만료시간, secret Key 설정한 후 build
     * @param intraId : token 주인을 payload에 설정하기 위함
     * @param validateTime : 토큰의 만료시간 설정하기 위함
     * @return token
     */
    private String createToken(final Integer intraId, final String intraName,final long validateTime) {
        final Claims claims = Jwts.claims().setSubject("User");
        claims.put("intraId", intraId);
        claims.put("intraName", intraName);
        claims.put("roles", "Cadet");
        Date now = new Date();

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
    public Authentication getAuthentication(final String token) {
        // 토큰 복호화
        Claims claims = parseToken(token);
        log.info("token 정보 : " + claims);

        if (claims.get("roles") == null) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        final Collection<? extends GrantedAuthority> authorities = Stream.of(
                        claims.get("roles").toString())
                .map(SimpleGrantedAuthority::new)
                .toList();

        final Integer intraId = claims.get("intraId", Integer.class);

        //token 에 담긴 정보에 맵핑되는 User 정보 디비에서 조회
        final Member member = memberService.findOne(intraId)
                .orElseThrow(MemberException.NoMemberException::new);

        //Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(member, "", authorities);
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
        log.info("토큰 검사해바라!!");
        log.info(accessToken);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateSecretKey(secret_code))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new JwtException.InvalidJwtToken();
        } catch (ExpiredJwtException e) {
            // 방법 2. 백에서 refresh 저장해놨다가 refresh 유효성 검사하고 access 재발급
            throw new JwtException.ExpiredJwtToken();
        } catch (UnsupportedJwtException e) {
            throw new JwtException.UnsupportedJwtToken();
        } catch (IllegalArgumentException e) {
            throw new JwtException.IllegalJwtToken();
        } catch (SignatureException e) {
            throw new JwtException.WrongSignedJwtToken();
        }
    }

    @Transactional
    public void save(final JsonWebToken jsonWebToken) {
        jwtRepository.save(jsonWebToken);
    }
}
