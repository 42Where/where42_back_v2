package kr.where.backend.announcement.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import kr.where.backend.announcement.Announcement;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateAnnouncementDTO {
    @NotBlank
    @Length(max = 200)
    private String title;
    @Length(max = 200)
    @NotBlank
    private String content;

    public Announcement toEntity(final AuthUser authUser) {
        return new Announcement(
            this.title,
            this.content,
            authUser.getIntraName(),
            LocalDateTime.now(),
            LocalDateTime.now());
    }
}
