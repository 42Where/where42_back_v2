package kr.where.backend.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReIssueDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private String refreshToken;
}
