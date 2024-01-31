package kr.where.backend.join;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.where.backend.api.exception.JsonException;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.jwt.dto.ResponseRefreshTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/join")
public class JoinController {
    private final JoinService joinService;

    @PostMapping("")
    public ResponseEntity<ResponseRefreshTokenDTO> join(@AuthUserInfo final AuthUser authUser) {

        return ResponseEntity.ok(joinService.join(authUser));
    }
}
