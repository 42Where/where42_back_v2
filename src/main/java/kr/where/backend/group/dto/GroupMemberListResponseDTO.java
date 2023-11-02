package kr.where.backend.group.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberListResponseDTO {
    private Long groupId;
    private String groupName;
    private int count;
    private List<FindGroupMemberResponseDTO> members;

    @Builder
    public GroupMemberListResponseDTO(Long groupId, String groupName, int count, List<FindGroupMemberResponseDTO> members){
        this.groupId = groupId;
        this.groupName = groupName;
        this.count = count;
        this.members = members;
    }
}
