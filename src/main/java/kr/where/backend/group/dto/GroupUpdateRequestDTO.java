package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupUpdateRequestDTO {
    @NotNull
    private Long groupId;
    @NotEmpty
    private String groupName;
}
