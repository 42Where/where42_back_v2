package kr.where.backend.group.dto.group;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private String groupName;

    @Builder
    public CreateGroupDTO(final Integer intraId, final String groupName) {
        this.intraId = intraId;
        this.groupName = groupName;
    }
}