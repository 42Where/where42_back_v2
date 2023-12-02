package kr.where.backend.member;

import jakarta.persistence.*;
import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.api.mappingDto.Planet;
import kr.where.backend.exception.request.RequestErrorCode;
import kr.where.backend.exception.request.RequestException;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.location.Location;
import kr.where.backend.api.mappingDto.Hane;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "intra_id", unique = true)
	private Long intraId;

	@Column(length = 15, unique = true, nullable = false)
	private String intraName;

	@Column(length = 40)
	private String comment;

	private String image;

	private boolean inCluster;

	@Column(nullable = false)
	private boolean blackHole;

	@Column(nullable = false)
	private String grade;

	@Column(nullable = false)
	private boolean agree;

	private Long defaultGroupId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDate createdAt = LocalDate.now();

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDate updatedAt = LocalDate.now();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<GroupMember> groupMembers = new ArrayList<>();

	public Member(final CadetPrivacy cadetPrivacy, final Hane hane) {
		this.intraId = cadetPrivacy.getId();
		this.intraName = cadetPrivacy.getLogin();
		this.grade = cadetPrivacy.getCreated_at();
		this.image = cadetPrivacy.getImage().getVersions().getSmall();
        this.inCluster = hane.getInoutState().equals("IN");
		this.agree = true;
		this.blackHole = false;
	}

	public Member(final CadetPrivacy cadetPrivacy) {
		this.intraId = cadetPrivacy.getId();
		this.intraName = cadetPrivacy.getLogin();
		this.image = cadetPrivacy.getImage().getVersions().getSmall();
		this.grade = cadetPrivacy.getCreated_at();
		this.blackHole = false;
		this.agree = false;
	}

	public void setFlashToMember(final CadetPrivacy cadetPrivacy, final Hane hane) {
		this.grade = cadetPrivacy.getCreated_at();
		this.inCluster = Objects.equals(hane.getInoutState(), "IN");
		this.agree = true;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}
	public void setLocation(final Location location) {
		this.location = location;
	}

	public void setDefaultGroupId(final Long defaultGroupId) {
		this.defaultGroupId = defaultGroupId;
	}

	public void setBlackHole(final boolean active) {
		this.blackHole = !active;
	}

	public void setInCluster(final Planet haneInfo) {
		this.inCluster = Objects.equals(Planet.gaepo, haneInfo);

		if (Objects.equals(Planet.error, haneInfo))
			throw new RequestException.HaneRequestException(RequestErrorCode.HANE_SERVICE);
	}

	public void setImage(final String image) {
		this.image = image;
	}

	/*
	 * 테스트용 setter
	 */
	public void setOtherInformation(final String comment, final boolean inCluster) {
		this.comment = comment;
		this.inCluster = inCluster;
	}
}
