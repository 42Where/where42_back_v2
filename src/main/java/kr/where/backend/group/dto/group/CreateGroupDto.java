package kr.where.backend.group.dto.group;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDto {

    private Integer memberIntraId;
    private String groupName;

    @Builder
    public CreateGroupDto(final Integer memberIntraId, final String groupName) {
        this.memberIntraId = memberIntraId;
        this.groupName = groupName;
    }
}