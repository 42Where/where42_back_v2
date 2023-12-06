package kr.where.backend.group.dto.groupmember;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindGroupMemberDto {
    private Integer memberId;
    private Long groupId;
    private Long defaultGroupId;

    @Builder
    public FindGroupMemberDto(
            final Integer memberId,
            final Long groupId,
            final Long defaultGroupId) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.defaultGroupId = defaultGroupId;
    }
}
