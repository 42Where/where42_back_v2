package kr.where.backend.announcement.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import kr.where.backend.announcement.Announcement;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateAnnouncementDto {
    @NotBlank
    private String title;
    @NotBlank
    private String comment;

    public static Announcement toEntity(CreateAnnouncementDto createAnnouncementDto, AuthUser authUser) {
        return new Announcement(
            createAnnouncementDto.getTitle(),
            createAnnouncementDto.getComment(),
            authUser.getIntraName(),
            LocalDate.now(),
            LocalDate.now());
    }
}
