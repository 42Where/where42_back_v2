package kr.where.backend.group.dto.groupmember;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseGroupMemberListDTO {
    private Long groupId;
    private String groupName;
    private int count;
    private List<FindResponseGroupMemberDTO> members;

    @Builder
    public ResponseGroupMemberListDTO(Long groupId, String groupName, int count, List<FindResponseGroupMemberDTO> members){
        this.groupId = groupId;
        this.groupName = groupName;
        this.count = count;
        this.members = members;
    }
}
