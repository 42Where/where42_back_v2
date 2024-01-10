package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteGroupMemberListDTO {
    @NotBlank
    private Long groupId;
    @NotBlank
    private List<Integer> members;

    @Builder
    public DeleteGroupMemberListDTO(Long groupId, List<Integer> members) {
        this.groupId = groupId;
        this.members = members;
    }
}
