package kr.where.backend.jwt.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.jwt.dto.RequestReissueDTO;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    ResponseEntity<ResponseAccessTokenDTO> reIssue(final HttpServletResponse response,
                                                   @RequestBody final RequestReissueDTO requestReissueDTO);
}
