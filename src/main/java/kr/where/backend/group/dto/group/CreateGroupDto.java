package kr.where.backend.group.dto.group;
import lombok.*;

@Getter
@NoArgsConstructor
public class CreateGroupDto {

    private Long memberIntraId;
    private String groupName;

    @Builder
    public CreateGroupDto(Long memberIntraId, String groupName) {
        this.memberIntraId = memberIntraId;
        this.groupName = groupName;
    }
}
