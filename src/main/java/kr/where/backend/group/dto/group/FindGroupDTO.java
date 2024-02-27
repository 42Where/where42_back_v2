package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindGroupDTO {
    @NotNull
    private Integer intraId;

    @Builder
    public FindGroupDTO(final Integer intraId) {
        this.intraId = intraId;
    }
}
