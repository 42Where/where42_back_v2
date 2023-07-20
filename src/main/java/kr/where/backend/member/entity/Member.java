package kr.where.backend.member.entity;

import jakarta.persistence.*;
import kr.where.backend.group.Groups;
import kr.where.backend.member.Enum.MemberLevel;
import kr.where.backend.member.Enum.Planet;
import kr.where.backend.member.DTO.Locate;
import kr.where.backend.utils.Define;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Member extends User {

    @Enumerated
    private MemberLevel level;
    private Long defaultGroupId;
    private Long starredGroupId;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Groups> groups = new ArrayList<Groups>();
    private String img;
    private String msg;
    private int inOrOut;
    @Embedded
    private Locate locate = new Locate(null, 0, 0, null);
    private String location;
    private String signUpDate;
    private Integer evaling;
    @Temporal(TemporalType.TIMESTAMP)
    Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    Date evalDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date updateTime;

    public Member(String name, String img, String location, String signUpDate, MemberLevel level) {
        this.name = name;
        this.img = img;
        this.location = location;
        this.signUpDate = signUpDate;
        this.level = level;
        this.evaling = 0;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public void setDefaultGroup(Long defaultGroupId, Long starredGroupId) {
        this.defaultGroupId = defaultGroupId;
        this.starredGroupId = starredGroupId;
    }

    public void updatePersonalMsg(String msg) {
        this.msg = msg;
    }

    public void changeTime() {
        this.updateTime = new Date();
    }

    public void updatePlanet(Planet planet) {
        log.info("[member-update] \"{}\"님의 Planet이 \"{}\"에서 \"{}\"(으)로 업데이트 되었습니다.", this.name, this.getLocate().getPlanet(), planet);
        this.getLocate().updatePlanet(planet);
        this.updateTime = new Date();
    }

    public void updateLocation(String location) {
        log.info("[member-update] \"{}\"님의 Location이 \"{}\"에서 \"{}\"(으)로 업데이트 되었습니다.", this.name, this.location, location);
        this.location = location;
        this.updateTime = new Date();
    }

    public void updateParsedInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
        this.location = Define.PARSED;
    }

    public void updateInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

    public void updateParsedStatus(int inOrOut) {
        this.inOrOut = inOrOut;
        this.location = Define.PARSED;
        this.updateTime = new Date();
    }

    public void updateOutStatus(int inOrOut) {
        this.inOrOut = inOrOut;
        this.updateTime = new Date();
    }

    public void updateEval(int status) {
        this.evaling = status;
        this.evalDate = new Date();
    }

    public Long timeDiff() {
        Date now = new Date();
        return (now.getTime() - updateTime.getTime()) / 60000;
    }


    public Long evalTimeDiff() {
        Date now = new Date();
        return (now.getTime() - evalDate.getTime())/ 60000;
    }

    public void updateSignUpDate(String date) {
        this.signUpDate = date;
    }

}
