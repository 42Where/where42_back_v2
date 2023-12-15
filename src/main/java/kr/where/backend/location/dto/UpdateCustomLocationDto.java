package kr.where.backend.location.dto;

import lombok.Getter;

@Getter
public class UpdateCustomLocationDto {
    private Integer intraId;
    private String customLocation;

    //Test
    public static UpdateCustomLocationDto createForTest(Integer intraId, String customLocation) {
        UpdateCustomLocationDto updateCustomLocationDto = new UpdateCustomLocationDto();

        updateCustomLocationDto.intraId = intraId;
        updateCustomLocationDto.customLocation = customLocation;

        return updateCustomLocationDto;
    }
}
