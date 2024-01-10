package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindGroupDTO {
    @NotBlank
    private Integer intraId;

    @Builder
    public FindGroupDTO(Integer intraId) {
        this.intraId = intraId;
    }
}
