package kr.where.backend.group.dto.group;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDTO {
    @NotBlank
    private String groupName;

    @Builder
    public CreateGroupDTO(final String groupName) {
        this.groupName = groupName;
    }
}