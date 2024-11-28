package kr.where.backend.announcement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnnouncementDto {
    @NotBlank
    private String title;
    @NotBlank
    private String comment;
}
