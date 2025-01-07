package kr.where.backend.logout.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "logout", description = "logout API")
public interface LogoutApiDocs {

    @Operation(summary = "3.1 logout API", description = "로그아웃을 위한 api",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                            content = @Content(schema = @Schema(implementation = Integer.class)))
            }

    )
    @PostMapping("/logout")
    ResponseEntity<Integer> logout(final HttpServletRequest request, @AuthUserInfo final AuthUser authUser);
}
