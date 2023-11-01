package kr.where.backend.member.DTO;

import lombok.Getter;

@Getter
public class CreateMemberDto {
    private Long intraId;
    private String intraName;
    private int grade;
    private String image;

    public static CreateMemberDto create(final Long intraId, final String intraName, final int grade, final String image){
        CreateMemberDto createMemberDto = new CreateMemberDto();

        createMemberDto.intraId = intraId;
        createMemberDto.intraName = intraName;
        createMemberDto.grade = grade;
        createMemberDto.image = image;

        return createMemberDto;
    }
}
