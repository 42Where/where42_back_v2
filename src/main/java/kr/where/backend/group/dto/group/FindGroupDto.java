package kr.where.backend.group.dto.group;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class FindGroupDto {
    private Integer intraId;

    @Builder
    public FindGroupDto(Integer intraId) {
        this.intraId = intraId;
    }
}
