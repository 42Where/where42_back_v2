package kr.where.backend.announcement.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementListDto {
    private List<ResponseAnnouncementDto> responseAnnouncementDto;

    public ResponseAnnouncementListDto(List<ResponseAnnouncementDto> responseAnnouncementDto) {
        this.responseAnnouncementDto = responseAnnouncementDto;
    }
}
