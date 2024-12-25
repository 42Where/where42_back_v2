package kr.where.backend.logout;

import jakarta.servlet.http.HttpServletRequest;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.redisToken.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final RedisTokenService redisTokenService;
    private final JwtService jwtService;

    public Integer logout(final HttpServletRequest request, final Integer intraId) {
        final String accessToken = jwtService.extractToken(request)
                .orElse(null);
        redisTokenService.invalidateToken(accessToken, intraId.toString());
        SecurityContextHolder.clearContext();
        return intraId;
    }
}
