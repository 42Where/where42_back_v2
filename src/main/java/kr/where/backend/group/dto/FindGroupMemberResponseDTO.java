package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class FindGroupMemberResponseDTO {
    @NotNull
    private Long id;

    @Builder
    public FindGroupMemberResponseDTO(Long id){
        this.id = id;
    }
}
