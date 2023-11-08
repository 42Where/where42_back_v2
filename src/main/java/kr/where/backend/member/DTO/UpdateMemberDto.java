package kr.where.backend.member.DTO;

import lombok.Getter;

@Getter
public class UpdateMemberDto {
	private Long intraId;
	private String comment;
	private String customLocation;
}
