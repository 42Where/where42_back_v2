package kr.where.backend.group.dto.group;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDto {

    private Integer IntraId;
    private String groupName;

    @Builder
    public CreateGroupDto(final Integer IntraId, final String groupName) {
        this.IntraId = IntraId;
        this.groupName = groupName;
    }
}