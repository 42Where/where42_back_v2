package kr.where.backend.redisToken;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String memberId, String accessToken, String refreshToken, long refreshTokenExpireTime) {
        // Access Token 저장
        redisTemplate.opsForValue().set("accessToken:" + memberId, accessToken, 30, TimeUnit.MINUTES);
        // Refresh Token 저장
        redisTemplate.opsForValue().set("refreshToken:" + memberId, refreshToken, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }

    public void saveAccessToken(String memberId, String accessToken) {
        // Access Token 저장
        redisTemplate.opsForValue().set("accessToken:" + memberId, accessToken, 30, TimeUnit.MINUTES);
    }

    public void saveRefreshToken(String memberId, String refreshToken, long refreshTokenExpireTime) {
        // Refresh Token 저장
        redisTemplate.opsForValue().set("refreshToken:" + memberId, refreshToken, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
    }


    public String getAccessToken(String memberId) {
        return (String) redisTemplate.opsForValue().get("accessToken:" + memberId);
    }

    public String getRefreshToken(String memberId) {
        return (String) redisTemplate.opsForValue().get("refreshToken:" + memberId);
    }

    public void deleteTokens(String memberId) {
        redisTemplate.delete("accessToken:" + memberId);
        redisTemplate.delete("refreshToken:" + memberId);
    }

    public boolean isTokenExists(String memberId, String token) {
        return token.equals(redisTemplate.opsForValue().get("accessToken:" + memberId)) ||
                token.equals(redisTemplate.opsForValue().get("refreshToken:" + memberId));
    }
}
