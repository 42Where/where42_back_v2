package kr.where.backend.cluster;

import jakarta.persistence.*;
import kr.where.backend.member.Member;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String cluster;

    private Integer row;

    private Integer seat;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // `Member`의 외래 키
    private Member member;

    public Cluster(String cluster, Integer row, Integer seat) {
        this.cluster = cluster;
        this.row = row;
        this.seat = seat;
    }

    public void updateMember(final Member member) {
        this.member = member;
    }
}
