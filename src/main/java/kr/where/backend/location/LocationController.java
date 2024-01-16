package kr.where.backend.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "location", description = "location API")
@RequestMapping("/v3/location")
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

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
			@ApiResponse(responseCode = "404", description = "멤버가 존재하지 않음", content = @Content(schema = @Schema(implementation = MemberException.class)))
		}
	)
	@PostMapping("/custom")
	public ResponseEntity updateCustomLocation(@RequestBody @Valid final UpdateCustomLocationDTO updateCustomLocation) {
		final AuthUserInfo authUser = AuthUserInfo.of();
		final ResponseLocationDTO responseLocationDto = locationService.updateCustomLocation(updateCustomLocation,
			authUser);

		return ResponseEntity.ok(responseLocationDto);
	}

}
