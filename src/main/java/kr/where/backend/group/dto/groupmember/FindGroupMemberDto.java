package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindGroupMemberDto {
    private Long memberId;
    private Long groupId;

    @Builder
    public FindGroupMemberDto(Long memberId, Long groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }
}
