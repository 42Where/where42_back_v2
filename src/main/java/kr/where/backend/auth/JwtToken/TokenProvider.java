package kr.where.backend.auth.JwtToken;

import com.nimbusds.oauth2.sdk.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class TokenProvider {
    private final Long expirationTime;
    private final String secretKey;
    private final String issuer;


    public TokenProvider() {
        this.secretKey = "ifhewiufghre'apiwhr134e134aeorgbosignspfjawfojo[fowefnipwnvs'mvs'lfmd";
        this.expirationTime = 3L;
        this.issuer = "where42";
    }

    public String createToken(final Long memberId) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setIssuer(issuer)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationTime, ChronoUnit.HOURS)))
                .claim("id", memberId)
                .compact();
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info(String.format("exception : %s, message : 잘못된 JWT 서명입니다.", e.getClass().getName()));
        } catch (ExpiredJwtException e) {
            log.info(String.format("exception : %s, message : 만료된 JWT 토큰입니다.", e.getClass().getName()));
        } catch (UnsupportedJwtException e) {
            log.info(String.format("exception : %s, message : 지원되지 않는 JWT 토큰입니다.", e.getClass().getName()));
        } catch (IllegalArgumentException e) {
            log.info(String.format("exception : %s, message : JWT 토큰이 잘못되었습니다.", e.getClass().getName()));
        }
        return false;
    }

    public Claims parseClaims(final String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(final String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);
        log.info("token_claims : " + claims.toString());

        if (claims.get("role") == null) {
            throw new BadCredentialsException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        final Collection<? extends GrantedAuthority> authorities = Stream.of(
                        claims.get("role").toString())
                .map(SimpleGrantedAuthority::new)
                .toList();

        final String userUuid = claims.get("userUuid").toString();

        //token 에 담긴 정보에 맵핑되는 User 정보 디비에서 조회
        final Member member = new Member("suhwpark", "....", "GEAPO", "20230810", MemberLevel.member);

        //Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(member, userUuid, authorities);
    }
}
