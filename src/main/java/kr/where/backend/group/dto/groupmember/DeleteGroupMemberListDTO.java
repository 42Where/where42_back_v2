package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteGroupMemberListDTO {
    @NotNull
    private Long groupId;
    @NotNull
    private List<Integer> members;

    @Builder
    public DeleteGroupMemberListDTO(final Long groupId, final List<Integer> members) {
        this.groupId = groupId;
        this.members = members;
    }
}
