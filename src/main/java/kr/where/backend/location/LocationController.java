package kr.where.backend.location;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.location.dto.ResponseImacUsageDTO;
import kr.where.backend.location.dto.ResponseLoggedImacListDTO;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.ResponseClusterUsageDTO;
import kr.where.backend.location.dto.ResponseClusterUsageListDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.location.swagger.LocationApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/v3/location")
@RequiredArgsConstructor
public class LocationController implements LocationApiDocs {

	private final LocationService locationService;

	/**
	 * 수동자리 업데이트
	 *
	 * @param updateCustomLocation
	 * @return ResponseEntity(responseLocationDTO)
	 */
	@PostMapping("/custom")
	public ResponseEntity<ResponseLocationDTO> updateCustomLocation(@RequestBody @Valid final UpdateCustomLocationDTO updateCustomLocation) {
		final AuthUser authUser = AuthUser.of();
		final ResponseLocationDTO responseLocationDto = locationService.updateCustomLocation(updateCustomLocation,
			authUser);

		return ResponseEntity.ok(responseLocationDto);
	}

	/**
	 * 수동자리 초기화(삭제)
	 *
	 * @return ResponseEntity(responseLocationDTO)
	 */
	@DeleteMapping("/custom")
	public ResponseEntity<ResponseLocationDTO> deleteCustomLocation(@AuthUserInfo final AuthUser authUser) {
		final ResponseLocationDTO responseLocationDto = locationService.deleteCustomLocation(authUser);

		return ResponseEntity.ok(responseLocationDto);
	}

	@GetMapping("/active/{cluster}")
	public ResponseEntity<ResponseLoggedImacListDTO> getLoggedInIMacs(@PathVariable("cluster") final String cluster,
																	  @AuthUserInfo final AuthUser authUser) {
		return ResponseEntity.ok(locationService.getLoggedInIMacs(authUser, cluster));
	}

	@GetMapping("/cluster/usage")
	public ResponseEntity<ResponseClusterUsageListDTO> getClusterImacUsage() {
		return ResponseEntity.ok(locationService.getClusterImacUsage());
	}


	@GetMapping("/cluster/imacUsage")
	public ResponseEntity<ResponseImacUsageDTO> getImacUsagePerHaneCount() {
		return ResponseEntity.ok(locationService.getImacUsagePerHaneCount());
	}
}
