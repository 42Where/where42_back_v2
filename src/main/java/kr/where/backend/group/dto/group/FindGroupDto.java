package kr.where.backend.group.dto.group;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class FindGroupDto {
    private Long memberId;

    @Builder
    public FindGroupDto(Long memberId) {
        this.memberId = memberId;
    }
}
