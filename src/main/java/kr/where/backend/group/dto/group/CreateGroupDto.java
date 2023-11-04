package kr.where.backend.group.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGroupDto {
    @NotNull
    private Long memberIntraId;
    @NotEmpty
    @Size(max = 40)
    private String groupName;
}
