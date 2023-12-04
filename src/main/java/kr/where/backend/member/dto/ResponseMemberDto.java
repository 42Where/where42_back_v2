package kr.where.backend.member.dto;

import kr.where.backend.location.Location;
import kr.where.backend.member.Member;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class ResponseMemberDto {
    private Long intraId;
    private String intraName;
    private String grade;
    private String image;
    private String comment;
    private boolean inCluster;
    private boolean agree;
    private Long defaultGroupId;
    private Location location;

    @Builder
    public ResponseMemberDto(final Member member) {
        this.intraId = member.getIntraId();
        this.intraName = member.getIntraName();
        this.grade = member.getGrade();
        this.image = member.getImage();
        this.comment = member.getComment();
        this.inCluster = member.isInCluster();
        this.agree = member.isAgree();
        this.defaultGroupId = member.getDefaultGroupId();
        this.location = member.getLocation();
    }

}
