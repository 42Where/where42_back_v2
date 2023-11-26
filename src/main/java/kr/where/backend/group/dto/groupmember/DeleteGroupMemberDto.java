package kr.where.backend.group.dto.groupmember;

import lombok.Data;

@Data
public class DeleteGroupMemberDto {
    private Long memberIntraId;
    private Long groupId;

}
