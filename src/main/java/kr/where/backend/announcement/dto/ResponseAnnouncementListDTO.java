package kr.where.backend.announcement.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementListDTO {
    private List<ResponseAnnouncementDTO> announcements;
    private int totalPages;
    private long totalElements;

    public ResponseAnnouncementListDTO(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO, final int totalPages, final long totalElements) {
        this.announcements = ResponseAnnouncementDTO;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static ResponseAnnouncementListDTO of(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO, final int totalPages, final long totalElements) {
        return new ResponseAnnouncementListDTO(ResponseAnnouncementDTO, totalPages, totalElements);
    }

    public static ResponseAnnouncementListDTO of(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO) {
        return new ResponseAnnouncementListDTO(ResponseAnnouncementDTO, 0, 0);
    }
}
