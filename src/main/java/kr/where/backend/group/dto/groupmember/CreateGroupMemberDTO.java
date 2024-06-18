package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupMemberDTO {
    @NotNull
    private Integer intraId;
    @NotNull
    private Long groupId;

    @Builder
    public CreateGroupMemberDTO(final Integer intraId, final Long groupId) {
        this.intraId = intraId;
        this.groupId = groupId;
    }
}
