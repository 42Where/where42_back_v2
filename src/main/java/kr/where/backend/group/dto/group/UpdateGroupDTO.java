package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGroupDTO {
    @NotBlank
    private Long groupId;
    @NotBlank
    private String groupName;
}
