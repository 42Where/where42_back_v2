package kr.where.backend.announcement.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementListDTO {
    private List<ResponseAnnouncementDTO> announcements;

    public ResponseAnnouncementListDTO(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO) {
        this.announcements = ResponseAnnouncementDTO;
    }

    public static ResponseAnnouncementListDTO of(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO) {
        return new ResponseAnnouncementListDTO(ResponseAnnouncementDTO);
    }
}
