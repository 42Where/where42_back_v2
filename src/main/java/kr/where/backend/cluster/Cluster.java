package kr.where.backend.cluster;

import jakarta.persistence.*;
import kr.where.backend.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Cluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String cluster;

    private Integer rowIndex;

    private Integer seat;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member lastUsedMember;

    private Long usedCount;

    public Cluster(final String cluster, final Integer row, final Integer seat) {
        this.cluster = cluster;
        this.rowIndex = row;
        this.seat = seat;
        this.lastUsedMember = null;
        this.usedCount = 0L;
    }

    public void increaseUsedCount() {
        this.usedCount++;
    }

    public void updateLastUsedMember(Member member) {
        if (!Objects.equals(this.lastUsedMember, member)) {
            this.lastUsedMember = member;
        }
    }

    public void removeLastUsedMember() {
        this.lastUsedMember = null;
    }
}
