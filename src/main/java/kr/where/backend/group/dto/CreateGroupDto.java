package kr.where.backend.group.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CreateGroupDto {
    @NotNull
    private Long memberIntraId;
    @NotEmpty
    @Size(max = 40)
    private String groupName;
}
