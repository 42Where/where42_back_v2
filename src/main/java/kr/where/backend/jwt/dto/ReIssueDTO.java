package kr.where.backend.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReIssueDTO {
    @NotNull
    private Integer intraId;
    @NotBlank
    private String refreshToken;
}
