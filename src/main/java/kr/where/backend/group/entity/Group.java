package kr.where.backend.group.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import kr.where.backend.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "groups")
public class Group {

    public static final String DEFAULT_GROUP = "default";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "group_name", length = 40)
    private  String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers = new ArrayList<>();

    public Group(final String groupName) {
        this.groupName = groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    public boolean isInGroup(final Member member){
        return this.groupMembers.contains(member);
    }

    public void addGroupMember(final GroupMember groupMember) {
        this.groupMembers.add(groupMember);
    }
}
