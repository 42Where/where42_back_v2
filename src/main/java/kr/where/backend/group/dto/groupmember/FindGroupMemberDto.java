package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
