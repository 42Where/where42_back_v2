package kr.where.backend.member.dto;

import kr.where.backend.location.Location;
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
	private Location location;

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

	public static CreateMemberDto createWithLocation(final Long intraId, final String intraName, final int grade,
										 final String image, final Location location) {
		CreateMemberDto createMemberDto = new CreateMemberDto();

		createMemberDto.intraId = intraId;
		createMemberDto.intraName = intraName;
		createMemberDto.grade = grade;
		createMemberDto.image = image;
		createMemberDto.agree = true;
		createMemberDto.location = location;

		return createMemberDto;
	}


	public static CreateMemberDto createFlash(final Long intraId, final String intraName) {
		CreateMemberDto createFlashMemberDto = new CreateMemberDto();

		createFlashMemberDto.intraId = intraId;
		createFlashMemberDto.intraName = intraName;
		createFlashMemberDto.agree = false;

		return createFlashMemberDto;
	}
}
