package kr.where.backend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemberDto {
	private Long intraId;
	private String intraName;
	private int grade;
	private String image;
	private boolean agree;

	public static CreateMemberDto create(final Long intraId, final String intraName, final int grade,
		final String image) {
		CreateMemberDto createMemberDto = new CreateMemberDto();

		createMemberDto.intraId = intraId;
		createMemberDto.intraName = intraName;
		createMemberDto.grade = grade;
		createMemberDto.image = image;
		createMemberDto.agree = true;

		return createMemberDto;
	}

	public static CreateMemberDto create_flash(final Long intraId, final String intraName) {
		CreateMemberDto createFlashMemberDto = new CreateMemberDto();

		createFlashMemberDto.intraId = intraId;
		createFlashMemberDto.intraName = intraName;
		createFlashMemberDto.agree = false;

		return createFlashMemberDto;
	}
}
