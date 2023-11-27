package kr.where.backend.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.jwt.dto.ReIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/jwt")
@RequiredArgsConstructor
@Tag(name = "jwt", description = "jwt API")
public class JsonWebTokenController {
    private final JsonWebTokenService jsonWebTokenService;

    @Operation(
            summary = "reissue accessToken of Expired time API",
            description = "accessToken을 재 발급",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "토큰 재발급 요청",
                    required = true, content = @Content(schema = @Schema(implementation = ReIssue.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "401", description = "토큰 유효성 검사 실패", content = @Content(schema =@Schema(implementation = ErrorResponse.class)))
            }

    )
    @PostMapping("/reissue")
    public ResponseEntity<String> reIssue(@RequestBody final ReIssue reIssue) {

        return ResponseEntity.ok(jsonWebTokenService.reissueAccessToken(reIssue));
    }
}
