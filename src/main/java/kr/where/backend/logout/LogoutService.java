package kr.where.backend.logout;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    public String logout(final AuthUserInfo authUser) {
        SecurityContextHolder.clearContext();

        return authUser.getIntraName();
    }
}
