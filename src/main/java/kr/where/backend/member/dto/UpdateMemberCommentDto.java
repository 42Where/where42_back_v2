package kr.where.backend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberCommentDto {
    private Integer intraId;
    private String comment;
}
