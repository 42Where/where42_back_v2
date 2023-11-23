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
    private Long memberId;
    private String comment;
    private String memberIntraName;
    private String customLocation;
    private String imacLocation;
    private int clusterLocation;
    private boolean inCluster;
    private String image;


    @Builder
    public ResponseGroupMemberDTO(@NotNull Long groupId, String groupName, @NotNull Long memberId, String comment, String memberIntraName, String customLocation, String imacLocation, int clusterLocation, boolean inCluster, String image) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberId = memberId;
        this.comment = comment;
        this.memberIntraName = memberIntraName;
        this.customLocation = customLocation;
        this.imacLocation = imacLocation;
        this.clusterLocation = clusterLocation;
        this.inCluster = inCluster;
        this.image = image;
    }
}
