package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UpdateGroupDto {
    @NotNull
    private Long groupId;
    @NotEmpty
    private String groupName;
}
