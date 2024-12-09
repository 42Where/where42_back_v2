package kr.where.backend.redisToken;

import kr.where.backend.auth.filter.JwtConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${accesstoken.expiration.time}")
    private long accessTokenExpirationTime;
    @Value("${refreshtoken.expiration.time}")
    private long refreshTokenExpirationTime;

    public RedisTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(final String intraId, final String accessToken, final String refreshToken) {
        // Access Token 저장
        redisTemplate.opsForValue().set("accessToken:" + intraId, accessToken, accessTokenExpirationTime, TimeUnit.MILLISECONDS);
        // Refresh Token 저장
        redisTemplate.opsForValue().set("refreshToken:" + intraId, refreshToken, refreshTokenExpirationTime, TimeUnit.MILLISECONDS);
    }

    public void saveAccessToken(final String intraId, final String accessToken) {
        // Access Token 저장
        redisTemplate.opsForValue()
                .set(
                        JwtConstants.ACCESS.getValue() + ":" + intraId,
                        accessToken,
                        accessTokenExpirationTime,
                        TimeUnit.MILLISECONDS
                );
    }

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

    public void saveInBlackList(final String intraId) {
        final String accessToken = getAccessToken(intraId);
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

    public boolean isAccessTokenInBlackList(final String intraId, final String token) {
        return Objects.equals(token, redisTemplate.opsForValue().get("expiredAccess:" + intraId));
    }

    public boolean isRefreshTokenInBlackList(final String intraId, final String token) {
        return Objects.equals(token, redisTemplate.opsForValue().get("expiredRefresh:" + intraId));
    }

    public String getAccessToken(final String intraId) {
        return (String) redisTemplate.opsForValue().get(JwtConstants.ACCESS.getValue() + ":" + intraId);
    }

    public String getRefreshToken(final String intraId) {
        return (String) redisTemplate.opsForValue().get(JwtConstants.REFRESH.getValue() + ":" + intraId);
    }

    public void deleteTokens(final String intraId) {
        redisTemplate.delete(JwtConstants.ACCESS.getValue() + ":" + intraId);
        redisTemplate.delete(JwtConstants.REFRESH.getValue() + ":" + intraId);
    }

    public boolean isTokenExists(String intraId, String token) {
        return token.equals(redisTemplate.opsForValue().get("accessToken:" + intraId)) ||
                token.equals(redisTemplate.opsForValue().get("refreshToken:" + intraId));
    }
}
