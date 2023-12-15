package kr.where.backend.location.dto;

import lombok.Getter;

@Getter
public class UpdateCustomLocationDto {
    private Long intraId;
    private String customLocation;

    //Test
    public static UpdateCustomLocationDto createForTest(Long intraId, String customLocation) {
        UpdateCustomLocationDto updateCustomLocationDto = new UpdateCustomLocationDto();

        updateCustomLocationDto.intraId = intraId;
        updateCustomLocationDto.customLocation = customLocation;

        return updateCustomLocationDto;
    }
}
