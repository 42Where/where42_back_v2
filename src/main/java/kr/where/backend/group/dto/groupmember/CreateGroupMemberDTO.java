package kr.where.backend.group.dto.groupmember;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupMemberDTO {

    private Long intraId;
    private Long groupId;
    private String groupName;
    @JsonProperty(value = "isOwner")
    private boolean isOwner;

    @Builder
    public CreateGroupMemberDTO(Long intraId, Long groupId, String groupName, Boolean isOwner) {
        this.intraId = intraId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.isOwner = isOwner;
    }
}
