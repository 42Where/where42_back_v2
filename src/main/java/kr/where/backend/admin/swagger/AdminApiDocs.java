package kr.where.backend.admin.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.admin.dto.RequestRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusDTO;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "admin" , description = "admin API")
public interface AdminApiDocs {
    @Operation(
            summary = "Admin status check API",
            description = "관리자인지 확인하는 API",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "관리자인지 확인 성공", content = @Content(schema = @Schema(implementation = ResponseRoleStatusDTO.class))),
            }
    )
    @GetMapping("/status")
    ResponseEntity<ResponseAdminStatusDTO> getAdminStatus(@AuthUserInfo final AuthUser authUser);
    ResponseEntity<ResponseRoleStatusDTO> getRoleStatus(@AuthUserInfo final AuthUser authUser);

    @Operation(
            summary = "Admin post API",
            description = "관리자 역할 변경하는 API",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "변경할 권한을 role에 입력", required = true, content = @Content(schema = @Schema(implementation = RequestRoleStatusDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "역할 변경 성공", content = @Content(schema = @Schema(implementation = ResponseRoleStatusDTO.class))),
                    @ApiResponse(responseCode = "403", description = "접근 권한 부족", content = @Content(schema = @Schema(type = "string")))
            }
    )
    @PostMapping("/status")
    ResponseEntity<ResponseRoleStatusDTO> changeAdminStatus(@RequestBody @Valid final RequestRoleStatusDTO requestRoleStatusDTO);
}
