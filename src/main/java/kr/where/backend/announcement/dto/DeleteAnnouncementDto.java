package kr.where.backend.announcement.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteAnnouncementDto {
    private Long announcementId;

    public DeleteAnnouncementDto (Long announcementId) {
        this.announcementId = announcementId;
    }
}
