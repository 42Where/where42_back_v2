package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestGroupMemberDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private Long groupId;

    @Builder
    public RequestGroupMemberDTO(Integer intraId, Long groupId) {
        this.intraId = intraId;
        this.groupId = groupId;
    }
}
