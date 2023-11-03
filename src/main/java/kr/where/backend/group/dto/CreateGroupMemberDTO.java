package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateGroupMemberDTO {

    @NotNull
    private Long intraId;
    @NotNull
    private Long groupId;
    @NotNull
    private String groupName;
    @NotNull
    private boolean isOwner;

    @Builder
    public CreateGroupMemberDTO(@NotNull Long intraId, @NotNull Long groupId, String groupName, @NotNull boolean isOwner) {
        this.intraId = intraId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.isOwner = isOwner;
    }
}
