package kr.where.backend.member;

import jakarta.persistence.*;
import kr.where.backend.api.exception.RequestException;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.location.Location;
import kr.where.backend.api.json.hane.Hane;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Member {
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String USER_ROLE = "USER";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "intra_id", unique = true)
	private Integer intraId;

	@Column(length = 15, unique = true, nullable = false)
	private String intraName;

	@Column(length = 40)
	private String comment;

	private String image;

	private boolean inCluster;

	private LocalDateTime inClusterUpdatedAt;

	@Column(nullable = false)
	private boolean blackHole;

	@Column(nullable = false)
	private String grade;

	@Column(nullable = false)
	private boolean agree;

	private Long defaultGroupId;

	private String role;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(nullable = false)
	@UpdateTimestamp
	private LocalDateTime updatedAt = LocalDateTime.now();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<GroupMember> groupMembers = new ArrayList<>();

	public Member(final CadetPrivacy cadetPrivacy, final Hane hane) {
		this.intraId = cadetPrivacy.getId();
		this.intraName = cadetPrivacy.getLogin();
		this.grade = cadetPrivacy.getCreated_at();
		this.image = cadetPrivacy.getImage().getVersions().getSmall();
		this.inCluster = hane.getInoutState().equals("IN");
		this.role = USER_ROLE;
		this.inClusterUpdatedAt = LocalDateTime.now();
		this.agree = true;
		this.blackHole = cadetPrivacy.isActive();
	}

	public Member(final CadetPrivacy cadetPrivacy) {
		this.intraId = cadetPrivacy.getId();
		this.intraName = cadetPrivacy.getLogin();
		this.image = cadetPrivacy.getImage().getVersions().getSmall();
		this.grade = cadetPrivacy.getCreated_at();
		this.role = USER_ROLE;
		this.blackHole = cadetPrivacy.isActive();
		this.agree = false;
	}

	public void setDisagreeToAgree(final Hane hane) {
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

	public void setInCluster(final Hane hane) {
		this.inCluster = Objects.equals(hane.getInoutState(), "IN");
		this.inClusterUpdatedAt = LocalDateTime.now();
	}

	public boolean isPossibleToUpdateInCluster() {
		if (inClusterUpdatedAt == null || LocalDateTime.now()
			.minusMinutes(5)
			.isAfter(inClusterUpdatedAt))
			return true;
		return false;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public void updateRole(final String role) {
		validateRole(role);
		this.role = role;
	}

	private void validateRole(final String role) {
		if (!role.equals(ADMIN_ROLE) && !role.equals(USER_ROLE)) {
			throw new RequestException.BadRequestException();
		}
	}
}
