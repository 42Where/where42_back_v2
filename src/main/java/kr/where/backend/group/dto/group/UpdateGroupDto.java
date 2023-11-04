package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroupDto {
    @NotNull
    private Long groupId;
    @NotEmpty
    private String groupName;
}
