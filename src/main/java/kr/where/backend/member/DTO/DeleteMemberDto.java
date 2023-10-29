package kr.where.backend.member.DTO;

import lombok.Getter;

@Getter
public class DeleteMemberDto {
    private Long intraId;

    public static DeleteMemberDto create(final Long intraId) {
        DeleteMemberDto deleteMemberDto = new DeleteMemberDto();

        deleteMemberDto.intraId = intraId;

        return deleteMemberDto;
    }
}
