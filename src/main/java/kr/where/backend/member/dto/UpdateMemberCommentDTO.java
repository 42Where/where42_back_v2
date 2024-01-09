package kr.where.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberCommentDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private String comment;
}
