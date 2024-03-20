package kr.where.backend.jwt.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import kr.where.backend.jwt.dto.ResponseRefreshTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "jwt", description = "jwt API")
public interface JwtApiDocs {
    @Operation(
            summary = "reissue accessToken of Expired time API",
            description = "accessToken을 재 발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = ResponseAccessTokenDTO.class))),
            }

    )
    @PostMapping("/reissue")
    ResponseEntity<ResponseAccessTokenDTO> reIssue(@AuthUserInfo AuthUser authUser);
}
