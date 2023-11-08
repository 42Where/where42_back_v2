package kr.where.backend.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "group_name", length = 40)
    private  String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<GroupMember> groupMembers = new HashSet<>();

    @Column(name = "is_default")
    private boolean isDefault;

    public Group(String groupName, Boolean isDefault) {
        this.groupName = groupName;
        this.isDefault = isDefault;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
