package kr.where.backend.group.dto.group;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class FindGroupDto {
    private Integer memberId;

    @Builder
    public FindGroupDto(Integer memberId) {
        this.memberId = memberId;
    }
}
