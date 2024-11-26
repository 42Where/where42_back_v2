package kr.where.backend.announcement.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseAnnouncementListDTO {
    private static final int UNUSED_TOTAL_PAGES = 0;
    private static final long UNUSED_TOTAL_ELEMENTS = 0L;

    private final List<ResponseAnnouncementDTO> announcements;
    private final int totalPages;
    private final long totalElements;

    public static ResponseAnnouncementListDTO of(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO, final int totalPages, final long totalElements) {
        return new ResponseAnnouncementListDTO(ResponseAnnouncementDTO, totalPages, totalElements);
    }

    public static ResponseAnnouncementListDTO of(final List<ResponseAnnouncementDTO> ResponseAnnouncementDTO) {
        return new ResponseAnnouncementListDTO(ResponseAnnouncementDTO, UNUSED_TOTAL_PAGES, UNUSED_TOTAL_ELEMENTS);
    }
}
