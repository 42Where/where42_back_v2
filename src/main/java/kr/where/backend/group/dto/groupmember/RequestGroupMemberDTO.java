package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestGroupMemberDTO {

    private Integer memberId;
    private Long groupId;

    @Builder
    public RequestGroupMemberDTO(Integer memberId, Long groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }
}
