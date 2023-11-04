package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDTO {
    @NotNull
    private Long memberId;

    @NotEmpty
    private String groupName;
}
