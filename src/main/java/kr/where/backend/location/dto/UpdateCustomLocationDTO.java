package kr.where.backend.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCustomLocationDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private String customLocation;

    //Test
    public static UpdateCustomLocationDTO createForTest(Integer intraId, String customLocation) {
        UpdateCustomLocationDTO updateCustomLocationDto = new UpdateCustomLocationDTO();

        updateCustomLocationDto.intraId = intraId;
        updateCustomLocationDto.customLocation = customLocation;

        return updateCustomLocationDto;
    }
}
