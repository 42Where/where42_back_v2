package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestGroupMemberDTO {

    private Integer intraId;
    private Long groupId;

    @Builder
    public RequestGroupMemberDTO(Integer intraId, Long groupId) {
        this.intraId = intraId;
        this.groupId = groupId;
    }
}
