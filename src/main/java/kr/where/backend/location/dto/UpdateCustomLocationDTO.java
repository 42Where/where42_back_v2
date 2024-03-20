package kr.where.backend.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCustomLocationDTO {
	private String customLocation;

	public static UpdateCustomLocationDTO createForTest(String customLocation) {
		UpdateCustomLocationDTO updateCustomLocationDto = new UpdateCustomLocationDTO();

		updateCustomLocationDto.customLocation = customLocation;

		return updateCustomLocationDto;
	}
}
