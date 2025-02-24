package kr.where.backend.group.dto.groupmember;

import kr.where.backend.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseOneGroupMemberDTO {

    private Integer intraId;
    private String intraName;
    private String grade;
    private String image;
    private String comment;
    private boolean inCluster;
    private boolean agree;
    private Long defaultGroupId;
    private String location;

    @Builder
    public ResponseOneGroupMemberDTO(
            final Integer intraId,
            final String intraName,
            final String grade,
            final String image,
            final String comment,
            final boolean inCluster,
            final boolean agree,
            final Long defaultGroupId,
            final String location) {
        this.intraId = intraId;
        this.intraName = intraName;
        this.grade = grade;
        this.image = image;
        this.comment = comment;
        this.inCluster = inCluster;
        this.agree = agree;
        this.defaultGroupId = defaultGroupId;
        this.location = location;
    }

    public static ResponseOneGroupMemberDTO of(final Member member) {
        return new ResponseOneGroupMemberDTO(
                member.getIntraId(),
                member.getIntraName(),
                member.getGrade(),
                member.getImage(),
                member.getComment(),
                member.isInCluster(),
                member.isAgree(),
                member.getDefaultGroupId(),
                member.getLocation().getLocation());
    }
}
