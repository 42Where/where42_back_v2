package kr.where.backend.group.dto.groupmember;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupMemberDTO {
    @NotBlank
    private Integer intraId;
    @NotBlank
    private Long groupId;

    @Builder
    public CreateGroupMemberDTO(final Integer intraId, final Long groupId) {
        this.intraId = intraId;
        this.groupId = groupId;
    }
}
