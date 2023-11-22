package kr.where.backend.member.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFlashMemberDto {
	private Long intraId;
	private String intraName;

	public static CreateFlashMemberDto create(final Long intraId, final String intraName) {
		CreateFlashMemberDto createFlashMemberDto = new CreateFlashMemberDto();

		createFlashMemberDto.intraId = intraId;
		createFlashMemberDto.intraName = intraName;

		return createFlashMemberDto;
	}
}
