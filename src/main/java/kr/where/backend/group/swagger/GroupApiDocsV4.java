package kr.where.backend.group.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.group.dto.group.ResponseOwnGroupMemberDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface GroupApiDocsV4 {
    @Operation(
            summary = "get own groups",
            description = "그룹 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 조회 성공", content = @Content(schema = @Schema(implementation = ResponseOwnGroupMemberDTO.class))),
            }
    )
    @GetMapping("")
    ResponseEntity<ResponseOwnGroupMemberDTO> findOwnGroups(@AuthUserInfo final AuthUser authUser);
}
