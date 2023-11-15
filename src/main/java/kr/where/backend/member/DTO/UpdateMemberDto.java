package kr.where.backend.member.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDto {
	private Long intraId;
	private String comment;
	private String customLocation;
}
