package kr.where.backend.redisToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.auth.filter.JwtConstants;
import kr.where.backend.jwt.exception.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${accesstoken.expiration.time}")
    private long accessTokenExpirationTime;
    @Value("${refreshtoken.expiration.time}")
    private long refreshTokenExpirationTime;

    public void saveRefreshToken(final String intraId, final String refreshToken) {
        // Refresh Token 저장
        redisTemplate.opsForValue()
                .set(
                         JwtConstants.REFRESH.getValue() + ":" + intraId,
                        refreshToken,
                        refreshTokenExpirationTime,
                        TimeUnit.MILLISECONDS
                );
    }

    public boolean isAccessTokenInBlackList(final String intraId, final String token) {
        return Objects.equals(token, redisTemplate.opsForValue().get("expiredAccess:" + intraId));
    }

    public boolean isRefreshTokenInBlackList(final String intraId, final String token) {
        return Objects.equals(token, redisTemplate.opsForValue().get("expiredRefresh:" + intraId));
    }

    public String getRefreshToken(final String intraId) {
        final String refreshToken = (String) redisTemplate.opsForValue()
                .get(JwtConstants.REFRESH.getValue() + ":" + intraId);
        if (Objects.equals(refreshToken, "null")) {
            throw new JwtException.ExpiredJwtToken();
        }
        return refreshToken;
    }

    public void invalidateToken(final String accessToken, final String intraId) {
        saveInBlackList(accessToken, intraId);
        deleteRefreshTokens(intraId);
    }

    private void saveInBlackList(final String accessToken, final String intraId) {
        final String refreshToken = getRefreshToken(intraId);
        redisTemplate.opsForValue()
                .set(
                        "expiredAccess:" + intraId,
                        accessToken,
                        accessTokenExpirationTime,
                        TimeUnit.MILLISECONDS
                );
        redisTemplate.opsForValue()
                .set(
                        "expiredRefresh:" + intraId,
                        refreshToken,
                        refreshTokenExpirationTime,
                        TimeUnit.MILLISECONDS
                );
    }

    private void deleteRefreshTokens(final String intraId) {
        redisTemplate.delete(JwtConstants.REFRESH.getValue() + ":" + intraId);
    }
}
