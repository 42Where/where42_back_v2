package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AddGroupMemberListDTO {
    @NotBlank
    private Long groupId;
    @NotBlank
    private List<String> members;

    @Builder
    public AddGroupMemberListDTO(final Long groupId, final List<String> members) {
        this.groupId = groupId;
        this.members = members;
    }
}
