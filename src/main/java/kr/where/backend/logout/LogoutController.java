package kr.where.backend.logout;

import jakarta.servlet.http.HttpServletRequest;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<Integer> logout(final HttpServletRequest request, @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.ok(logoutService.logout(request, authUser.getIntraId()));
    }
}
