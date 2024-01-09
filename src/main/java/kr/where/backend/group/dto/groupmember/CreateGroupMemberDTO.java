package kr.where.backend.group.dto.groupmember;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupMemberDTO {

    private Integer intraId;
    private Long groupId;
    private String groupName;

    @Builder
    public CreateGroupMemberDTO(
            final Integer intraId,
            final Long groupId,
            final String groupName,
            final Boolean isOwner) {
        this.intraId = intraId;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
