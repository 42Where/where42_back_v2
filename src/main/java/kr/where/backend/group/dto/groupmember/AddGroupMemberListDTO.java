package kr.where.backend.group.dto.groupmember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AddGroupMemberListDTO {
    private Long groupId;
    private List<String> members;

    @Builder
    public AddGroupMemberListDTO(final Long groupId, final List<String> members) {
        this.groupId = groupId;
        this.members = members;
    }
}
