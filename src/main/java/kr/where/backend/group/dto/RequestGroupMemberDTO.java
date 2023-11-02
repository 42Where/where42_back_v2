package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestGroupMemberDTO {
    @NotNull
    private Long groupId;
    @NotNull
    private Long memberId;
    @NotNull
    private Boolean is_owner;
}
