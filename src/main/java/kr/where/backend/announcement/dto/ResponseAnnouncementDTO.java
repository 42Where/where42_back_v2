package kr.where.backend.announcement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.where.backend.announcement.Announcement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementDTO {
    private Long announcementId;
    private String title;
    private String content;
    private String authorName;
    private LocalDate createAt;
    private LocalDate updateAt;

    public ResponseAnnouncementDTO(final Long announcementId,
                                   final String title,
                                   final String content,
                                   final String authorName,
                                   final LocalDateTime createAt,
                                   final LocalDateTime updateAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createAt = createAt.toLocalDate();
        this.updateAt = updateAt.toLocalDate();
    }

    public static ResponseAnnouncementDTO of(final Announcement savedAnnouncement) {
        return new ResponseAnnouncementDTO(
                savedAnnouncement.getId(),
                savedAnnouncement.getTitle(),
                savedAnnouncement.getContent(),
                savedAnnouncement.getAuthorName(),
                savedAnnouncement.getCreateAt(),
                savedAnnouncement.getUpdateAt());
    }
}
