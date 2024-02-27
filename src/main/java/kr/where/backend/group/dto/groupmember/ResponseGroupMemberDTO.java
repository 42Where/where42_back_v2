package kr.where.backend.group.dto.groupmember;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ResponseGroupMemberDTO {

    private Long groupId;
    private String groupName;
    private Integer intraId;
    private String comment;
    private String memberIntraName;
    private String location;
    private boolean inCluster;
    private String image;


    @Builder
    public ResponseGroupMemberDTO(
            final @NotNull Long groupId,
            final String groupName,
            final @NotNull Integer intraId,
            final String comment,
            final String memberIntraName,
            final String location,
            final boolean inCluster,
            final String image) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.intraId = intraId;
        this.comment = comment;
        this.memberIntraName = memberIntraName;
        this.location = location;
        this.inCluster = inCluster;
        this.image = image;
    }
}
