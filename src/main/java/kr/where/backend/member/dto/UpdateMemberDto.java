package kr.where.backend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDto {
    private Long intraId;
    private String comment;
}
