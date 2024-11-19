package kr.where.backend.announcement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NotBlank
public class CreateAnnouncementDto {
    private String title;
    private String comment;
}
