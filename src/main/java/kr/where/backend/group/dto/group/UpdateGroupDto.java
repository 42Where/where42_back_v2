package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupDto {
    @NotNull
    private Long groupId;
    @NotEmpty
    @Size(max = 40)
    private String groupName;
}
