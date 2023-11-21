package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindGroupMemberDto {
    private Long memberId;
    private Long groupId;
    private Long defaultGroupId;

    @Builder
    public FindGroupMemberDto(Long memberId, Long groupId, Long defaultGroupId) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.defaultGroupId = defaultGroupId;
    }
}
