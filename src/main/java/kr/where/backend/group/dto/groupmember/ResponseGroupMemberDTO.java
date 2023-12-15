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
    private Integer memberId;
    private String comment;
    private String memberIntraName;
    private String location;
    private boolean inCluster;
    private String image;


    @Builder
    public ResponseGroupMemberDTO(@NotNull Long groupId, String groupName, @NotNull Integer memberId, String comment, String memberIntraName, String location, boolean inCluster, String image) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberId = memberId;
        this.comment = comment;
        this.memberIntraName = memberIntraName;
        this.location = location;
        this.inCluster = inCluster;
        this.image = image;
    }
}
