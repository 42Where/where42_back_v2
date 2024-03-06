package kr.where.backend.update.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "update", description = "update API")

public interface UpdateApiDocs {
    @Operation(summary = "3.1 updateMember location API", description = "맴버 자리를 업데이트하는 api",
            responses = {
                    @ApiResponse(responseCode = "200", description = "업데이트 성공",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "업데이트 실패",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }

    )
    @PostMapping("")
    ResponseEntity<String> update(@AuthUserInfo final AuthUser authUser);
}
