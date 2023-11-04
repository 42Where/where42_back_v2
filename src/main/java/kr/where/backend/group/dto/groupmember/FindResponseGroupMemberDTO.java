package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class FindResponseGroupMemberDTO {
    @NotNull
    private Long id;

    @Builder
    public FindResponseGroupMemberDTO(Long id){
        this.id = id;
    }
}
