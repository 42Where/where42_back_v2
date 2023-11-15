package kr.where.backend.group.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
public class FindGroupDto {
    private Long memberId;

    @Builder
    public FindGroupDto(Long memberId) {
        this.memberId = memberId;
    }
}
