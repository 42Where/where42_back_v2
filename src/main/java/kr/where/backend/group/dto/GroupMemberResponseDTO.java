package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class GroupMemberResponseDTO {

    @NotNull
    private Long groupId;
    private String groupName;

    @Builder
    public GroupMemberResponseDTO(Long groupId, String groupName){
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
