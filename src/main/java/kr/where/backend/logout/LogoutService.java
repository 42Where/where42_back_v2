package kr.where.backend.logout;

import kr.where.backend.auth.authUser.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    public String logout(final AuthUser authUser) {
        SecurityContextHolder.clearContext();

        return authUser.getIntraName();
    }
}
