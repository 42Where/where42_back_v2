package kr.where.backend.announcement.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementDto {
    private Long announcementId;
    private String title;
    private String content;
    private String authorName;
    private LocalDate createAt;
    private LocalDate updateAt;
}
