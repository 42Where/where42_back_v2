package kr.where.backend.group.dto.group;

import kr.where.backend.group.entity.Group;
import lombok.Getter;

@Getter
public class ResponseGroupDTO {
    private Long groupId;
    private String groupName;

    private ResponseGroupDTO(final Long groupId, final String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public static final ResponseGroupDTO from(final Group group) {
        return new ResponseGroupDTO(group.getGroupId(), group.getGroupName());
    }
}
