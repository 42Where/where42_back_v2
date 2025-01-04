package kr.where.backend.seatHistory;

import jakarta.persistence.*;
import kr.where.backend.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String imac;

    private Long count;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "intra_id")
    private Member member;

    public SeatHistory(final String imac, final Member member) {
        this.imac = imac;
        this.count = 1L;
        this.member = member;
        this.updateAt = LocalDateTime.now();
    }

    public void increaseCount() {
        if (!isAfter5Minute()) {
            return;
        }
        this.count++;
        this.updateAt = LocalDateTime.now();
    }

    public boolean isAfter5Minute() {
        return LocalDateTime.now()
                .minusMinutes(5)
                .isAfter(updateAt);
    }
}
