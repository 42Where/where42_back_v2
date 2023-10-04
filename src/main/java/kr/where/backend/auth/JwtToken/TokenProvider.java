package kr.where.backend.auth.JwtToken;

import com.nimbusds.oauth2.sdk.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.where.backend.auth.exception.TokenExceptions;
import kr.where.backend.member.Enum.MemberLevel;
import kr.where.backend.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import java.nio.file.attribute.UserPrincipal;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class TokenProvider {
    private final Key secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final String issuer;

    public TokenProvider(@Value("${jwt.token.secret}") final String secretCode,
                         @Value("${accesstoken.expiration.time}") final long accessTokenExpirationTime,
                         @Value("${refreshtoken.expiration.time}") final long refreshTokenExpirationTime,
                         @Value("${issuer}") final String issuer) {
        this.secretKey = generateSecretKey(secretCode);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.issuer = issuer;
    }

    public String createAccessToken(final String intraId) {
        return createToken(intraId, accessTokenExpirationTime);
    }

    public String createRefreshToken(final String intraId) {
        return createToken(intraId, refreshTokenExpirationTime);
    }

    private Key generateSecretKey(final String secretCode) {
        final String encodedSecretCode = Base64.getEncoder().encodeToString(secretCode.getBytes());
        return Keys.hmacShaKeyFor(encodedSecretCode.getBytes());
    }

    private String createToken(final String intraId, final long validateTime) {
        final Claims claims = Jwts.claims().setSubject("User");
        claims.put("intraId", intraId);
        claims.put("roles", "Cadet");
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + validateTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(final String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new TokenExceptions.InvalidedTokenException();
        } catch (ExpiredJwtException e) {
            throw new TokenExceptions.ExpiredTokenTimeOutException();
        } catch (UnsupportedJwtException e) {
            throw new TokenExceptions.UnsupportedTokenException();
        } catch (IllegalArgumentException e) {
            throw new TokenExceptions.IllegalTokenException();
        }
    }

    public Authentication getAuthentication(final String token) {
        // 토큰 복호화
        Claims claims = parseToken(token);

        if (claims.get("role") == null) {
            throw new BadCredentialsException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        final Collection<? extends GrantedAuthority> authorities = Stream.of(
                        claims.get("role").toString())
                .map(SimpleGrantedAuthority::new)
                .toList();

        final String intraId = claims.get("intraId", String.class);

        //token 에 담긴 정보에 맵핑되는 User 정보 디비에서 조회
        final Member member = new Member(claims.getId(), "", "", "", MemberLevel.member);

        //Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(member, "", authorities);
    }
}
