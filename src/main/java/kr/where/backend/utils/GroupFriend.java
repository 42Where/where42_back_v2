package kr.where.backend.utils;

import jakarta.persistence.*;
import kr.where.backend.group.Groups;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "GROUPFRIEND_SEQ_GENERATOR",
        sequenceName = "GROUP_FRIEND_SEQ",
        initialValue = 1, allocationSize = 1
)
public class GroupFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUP_FRIEND_SEQ")
    @Column(name = "group_friend_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Groups group;
    @Column(nullable = false)
    private String friendName;
    private String img;
    private String signUpDate;
    @Column(name="add_at")
    private Date addAt;

    public GroupFriend(String friendName, String img, String signUpdate, Groups group) {
        this.friendName = friendName;
        this.img = img;
        this.signUpDate = signUpdate;
        this.group = group;
        this.addAt = new Date();
    }

    public GroupFriend(String friendName, Groups group) {
        this.friendName = friendName;
        this.group = group;
    }

    public void updateSignUpDate(String date) {
        this.signUpDate = date;
    }
}
