package kr.where.backend.group.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RequestGroupMemberDTO {

    private Long memberId;
    private Long groupId;

    @Builder
    public RequestGroupMemberDTO(Long memberId, Long groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }
}
