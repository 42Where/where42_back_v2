package kr.where.backend.member.DTO;

import kr.where.backend.location.Location;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class ResponseMemberDto {
	private Long intraId;
	private String intraName;
	private int grade;
	private String image;
	private String comment;
	private boolean inCluster;
	private boolean agree;
	private Long defaultGroupId;
	private Location location;

	@Builder
	public ResponseMemberDto(final Long intraId, final String intraName,
		final int grade, final String image, final String comment, final boolean inCluster, final boolean agree, final Long defaultGroupId, final Location location) {
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

}
