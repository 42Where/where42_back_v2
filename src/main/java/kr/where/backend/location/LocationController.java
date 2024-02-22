package kr.where.backend.location;

import jakarta.validation.Valid;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.location.swagger.LocationApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
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
	public ResponseEntity updateCustomLocation(@RequestBody @Valid final UpdateCustomLocationDTO updateCustomLocation) {
		final AuthUser authUser = AuthUser.of();
		final ResponseLocationDTO responseLocationDto = locationService.updateCustomLocation(updateCustomLocation,
			authUser);

		return ResponseEntity.ok(responseLocationDto);
	}

}
