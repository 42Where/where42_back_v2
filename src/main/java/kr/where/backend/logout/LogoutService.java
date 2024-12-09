package kr.where.backend.logout;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.redisToken.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final RedisTokenService redisTokenService;
    public String logout(final AuthUser authUser) {
        SecurityContextHolder.clearContext();

        redisTokenService.deleteTokens(authUser.getIntraId().toString());
        //여기서 블랙리스트 추가 해야할 듯 합니다.
        redisTokenService.saveInBlackList(authUser.getIntraId().toString());
        return authUser.getIntraName();
    }
}
