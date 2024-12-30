package kr.where.backend.location.swagger;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.location.dto.ResponseLoggedImacListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.exception.MemberException;

@Tag(name = "location", description = "location API")
public interface LocationApiDocs {
	@Operation(summary = "3.1 updateCustomLocation API", description = "수동 자리 업데이트",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(
				schema = @Schema(implementation = UpdateCustomLocationDTO.class))
		),
		responses = {
			@ApiResponse(responseCode = "200", description = "수동자리 변경 성공", content = @Content(schema = @Schema(implementation = ResponseLocationDTO.class))),
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@PostMapping("/custom")
	ResponseEntity<ResponseLocationDTO> updateCustomLocation(@RequestBody @Valid final UpdateCustomLocationDTO updateCustomLocation);

	@Operation(summary = "3.2 deleteCustomLocation API", description = "수동 자리 초기화(삭제)",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "수동자리 초기화 성공", content = @Content(schema = @Schema(implementation = ResponseLocationDTO.class))),
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@DeleteMapping("/custom")
	ResponseEntity<ResponseLocationDTO> deleteCustomLocation();

	@Operation(summary = "loggedIMac API", description = "클러스터별 imac 로그인한 사람 조회",
			parameters = {
					@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "imac에 로그인한 멤버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseLoggedImacListDTO.class))),
			}
	)
	@GetMapping("/{cluster}")
	public ResponseEntity<ResponseLoggedImacListDTO> getLoggedInIMacs(@PathVariable("cluster") final String cluster, @AuthUserInfo final AuthUser authUser);

}
