package kr.where.backend.group.dto.groupmember;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupMemberResponseDTO {
    private Long groupId;
    private String groupName;
    private int count;
    private List<Long> members;
}
