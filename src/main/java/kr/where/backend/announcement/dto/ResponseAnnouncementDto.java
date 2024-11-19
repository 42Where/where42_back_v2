package kr.where.backend.announcement.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ResponseAnnouncementDto {
    private Long announcementId;
    private String title;
    private String content;
    private String authorName;
    private LocalDate createAt;
    private LocalDate updateAt;
}
