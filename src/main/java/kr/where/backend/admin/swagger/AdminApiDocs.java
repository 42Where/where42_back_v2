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
import kr.where.backend.admin.dto.ResponseRoleStatusListDTO;
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
            description = "본인 접근 권한 레벨 확인하는 API",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "접근 권한 확인 성공", content = @Content(schema = @Schema(implementation = ResponseRoleStatusDTO.class))),
            }
    )
    @GetMapping("/status")
    ResponseEntity<ResponseRoleStatusDTO> getRoleStatus(@AuthUserInfo final AuthUser authUser);

    @Operation(
            summary = "get all admin API",
            description = "관리자인 유저 전체 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseRoleStatusListDTO.class)))
            }
    )
    @GetMapping("/status/all")
    ResponseEntity<ResponseRoleStatusListDTO> getAllAdmin();

    @Operation(
            summary = "Admin post API",
            description = "권한 변경하는 API",
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
