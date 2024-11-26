package kr.where.backend.announcement.dto;

import java.time.LocalDateTime;
import kr.where.backend.announcement.Announcement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnnouncementDto {
    private Long announcementId;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public ResponseAnnouncementDto(final Long announcementId, final String title, final String content, final String authorName, final LocalDateTime createAt, final LocalDateTime updateAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static ResponseAnnouncementDto of(final Announcement savedAnnouncement) {
        return ResponseAnnouncementDto.builder()
                .announcementId(savedAnnouncement.getId())
                .title(savedAnnouncement.getTitle())
                .content(savedAnnouncement.getContent())
                .authorName(savedAnnouncement.getAuthorName())
                .createAt(savedAnnouncement.getCreateAt())
                .updateAt(savedAnnouncement.getUpdateAt())
                .build();
    }
}
