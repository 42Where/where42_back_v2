package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class ResponseGroupMemberDTO {

    @NotNull
    private Long groupId;
    private String groupName;
    @NotNull
    private Long memberId;

    @Builder
    public ResponseGroupMemberDTO(Long groupId, String groupName, Long memberId){
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberId = memberId;
    }
}
