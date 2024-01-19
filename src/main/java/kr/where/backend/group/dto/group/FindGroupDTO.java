package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindGroupDTO {
    @NotNull
    private Integer intraId;

    @Builder
    public FindGroupDTO(Integer intraId) {
        this.intraId = intraId;
    }
}
