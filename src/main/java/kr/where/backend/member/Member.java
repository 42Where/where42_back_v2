package kr.where.backend.member;

import jakarta.persistence.*;
// import kr.where.backend.group.GroupMember;
import kr.where.backend.member.DTO.CreateMemberDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", unique = true, nullable = false)
	private Long id;

    @Column(unique = true, nullable = false)
    private Long intraId;

    @Column(length = 15, unique = true, nullable = false)
    private String intraName;

    @Column(length = 40)
    private String comment;

    private String image;

    private boolean inCluster;

    @Column(length = 6)
    private String imacLocation;

    @Column(length = 30)
    private String customLocation;

    private int clusterLocation;

//    @Builder.Default
    private boolean blackHole = true;

    @Column(nullable = false)
    private int grade;

//    @Builder.Default
    private boolean agree = false;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt = LocalDate.now();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<GroupMember> groupMembers = new ArrayList<>();

    public Member(final CreateMemberDto createMemberDto) {
        this.intraId = createMemberDto.getIntraId();
        this.intraName = createMemberDto.getIntraName();
        this.grade = createMemberDto.getGrade();
        this.image = createMemberDto.getImage();
        this.agree = true;
    }

    public void updatePersonalMsg(final String comment) {
        this.comment = comment;
    }

    public void updateCustomLocation(String customLocation) {
//        log.info("[member-update] \"{}\"님의 Location이 \"{}\"에서 \"{}\"(으)로 업데이트 되었습니다.", this.name, this.location, location);
        this.customLocation = customLocation;
    }

//
//    public void setDefaultGroup(Long defaultGroupId, Long starredGroupId) {
//        this.defaultGroupId = defaultGroupId;
//        this.starredGroupId = starredGroupId;
//    }
//
//
//    public void changeTime() {
//        this.updateTime = new Date();
//    }
//
//    public void updatePlanet(Planet planet) {
//        log.info("[member-update] \"{}\"님의 Planet이 \"{}\"에서 \"{}\"(으)로 업데이트 되었습니다.", this.name, this.getLocate().getPlanet(), planet);
//        this.getLocate().updatePlanet(planet);
//        this.updateTime = new Date();
//    }
//

//
//    public void updateParsedInOrOut(int inOrOut) {
//        this.inOrOut = inOrOut;
//        this.location = Define.PARSED;
//    }
//
//    public void updateInOrOut(int inOrOut) {
//        this.inOrOut = inOrOut;
//    }
//
//    public void updateParsedStatus(int inOrOut) {
//        this.inOrOut = inOrOut;
//        this.location = Define.PARSED;
//        this.updateTime = new Date();
//    }
//
//    public void updateOutStatus(int inOrOut) {
//        this.inOrOut = inOrOut;
//        this.updateTime = new Date();
//    }
//
//    public void updateEval(int status) {
//        this.evaling = status;
//        this.evalDate = new Date();
//    }
//
//    public Long timeDiff() {
//        Date now = new Date();
//        return (now.getTime() - updateTime.getTime()) / 60000;
//    }
//
//
//    public Long evalTimeDiff() {
//        Date now = new Date();
//        return (now.getTime() - evalDate.getTime())/ 60000;
//    }
//
//    public void updateSignUpDate(String date) {
//        this.signUpDate = date;
//    }

}
