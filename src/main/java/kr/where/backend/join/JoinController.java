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
import kr.where.backend.join.dto.ResponseJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/join")
@Tag(name = "join", description = "join API")
public class JoinController {
    private final JoinService joinService;

    @Operation(summary = "3.1 JoinMember API", description = "동의 맴버 생성하는 Post API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 생성 성공",
                            content = @Content(schema = @Schema(implementation = ResponseJoinDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 생성 실패",
                            content = @Content(schema = @Schema(implementation = JsonException.class)))
            }

    )
    @PostMapping("")
    public ResponseEntity<ResponseJoinDTO> join(
            final HttpServletRequest request,
            @AuthUserInfo final AuthUser authUser) {

        return ResponseEntity.ok(joinService.join(authUser));
    }
}
