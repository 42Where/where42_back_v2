package kr.where.backend.logout;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(logoutService.logout(AuthUserInfo.of()));
    }
}
