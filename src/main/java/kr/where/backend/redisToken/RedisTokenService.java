package kr.where.backend.redisToken;

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
    private final static String NULL = "null";
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

    public boolean isAccessTokenInBlackList(final String token) {
        return !Objects.equals(redisTemplate.opsForValue().get(token), NULL);
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
        redisTemplate.opsForValue()
                .set(
                        accessToken,
                        "expiredAccess:" + intraId,
                        accessTokenExpirationTime,
                        TimeUnit.MILLISECONDS
                );
    }

    private void deleteRefreshTokens(final String intraId) {
        redisTemplate.delete(JwtConstants.REFRESH.getValue() + ":" + intraId);
    }
}
