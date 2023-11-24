package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteGroupMemberListDto {
    private Long groupId;
    private List<Long> members;

    @Builder
    public DeleteGroupMemberListDto(Long groupId, List<Long> members) {
        this.groupId = groupId;
        this.members = members;
    }
}
