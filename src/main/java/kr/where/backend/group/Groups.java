package kr.where.backend.group;

import jakarta.persistence.*;
import kr.where.backend.member.entity.Member;
import kr.where.backend.utils.GroupFriend;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "GROUPS_SEQ_GENERATOR",
        sequenceName = "GROUPS_SEQ",
        initialValue = 1, allocationSize = 1
)
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUPS_SEQ")
    @Column(name = "group_id")
    private Long id;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupFriend> groupFriend = new ArrayList<GroupFriend>();

    @Column(nullable = false)
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    public Groups (String groupName, Member owner) {
        this.owner = owner;
        this.groupName = groupName;
    }

    public void updateGroupName(String groupName) {
        this.groupName = groupName;
    }
}
