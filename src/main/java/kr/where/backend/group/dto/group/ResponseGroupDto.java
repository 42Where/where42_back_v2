package kr.where.backend.group.dto.group;

import kr.where.backend.group.entity.Group;
import lombok.Getter;

@Getter
public class ResponseGroupDto {
    private Long groupId;
    private String groupName;

    private ResponseGroupDto(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public static final ResponseGroupDto from(final Group group) {
        return new ResponseGroupDto(group.getGroupId(), group.getGroupName());
    }
}
