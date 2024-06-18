package kr.where.backend.join;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.join.swagger.JoinApiDocs;
import kr.where.backend.jwt.dto.ResponseRefreshTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/join")
public class JoinController implements JoinApiDocs {
    private final JoinService joinService;

    @PostMapping("")
    public ResponseEntity<ResponseRefreshTokenDTO> join(@AuthUserInfo final AuthUser authUser) {

        return ResponseEntity.ok(joinService.join(authUser));
    }
}
