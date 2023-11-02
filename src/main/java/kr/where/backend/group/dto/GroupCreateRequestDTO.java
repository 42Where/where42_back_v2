package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupCreateRequestDTO {
    @NotNull
    private Long memberId;
    @NotNull
    private Long intraId;
    @NotEmpty
    private String groupName;
}
