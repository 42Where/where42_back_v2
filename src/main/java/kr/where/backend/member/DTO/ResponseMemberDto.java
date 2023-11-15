package kr.where.backend.member.DTO;

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
	private String imacLocation;
	private String customLocation;

	@Builder
	public ResponseMemberDto(final Long intraId, final String intraName,
		final int grade, final String image, final String comment, final String customLocation, final boolean inCluster,
		final String imacLocation) {
		this.intraId = intraId;
		this.intraName = intraName;
		this.grade = grade;
		this.image = image;
		this.comment = comment;
		this.customLocation = customLocation;
		this.inCluster = inCluster;
		this.imacLocation = imacLocation;
	}

}
