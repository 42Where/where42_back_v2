package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroupDTO {
    @NotNull
    private Long groupId;
    @NotBlank
    private String groupName;
}
