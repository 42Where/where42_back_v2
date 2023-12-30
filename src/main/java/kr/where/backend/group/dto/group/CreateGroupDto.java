package kr.where.backend.group.dto.group;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDto {

    private Integer intraId;
    private String groupName;

    @Builder
    public CreateGroupDto(final Integer intraId, final String groupName) {
        this.intraId = intraId;
        this.groupName = groupName;
    }
}