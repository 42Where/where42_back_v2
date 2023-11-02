package kr.where.backend.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.where.backend.member.Member;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    @Column(name = "is_owner")
    private Boolean isOwner;

    public GroupMember() {}
    public GroupMember(Group group, Member member, Boolean isOwner){
        this.group = group;
        this.member = member;
        this.isOwner = isOwner;
    }
}
