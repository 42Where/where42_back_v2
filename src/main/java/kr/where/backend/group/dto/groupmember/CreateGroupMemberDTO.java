package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateGroupMemberDTO {

    private Long intraId;
    private Long groupId;
    private String groupName;
    private boolean isOwner;

    @Builder
    public CreateGroupMemberDTO(Long intraId, Long groupId, String groupName, boolean isOwner) {
        this.intraId = intraId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.isOwner = isOwner;
    }
}
