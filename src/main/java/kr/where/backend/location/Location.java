package kr.where.backend.location;

import jakarta.persistence.*;
import kr.where.backend.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Location {

    @Id
    @Column(name = "location_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @OneToOne(mappedBy = "location")
    private Member member;

    @Column(length = 30)
    private String customLocation;

    @Column(length = 6)
    private String imacLocation;

    @Column(nullable = false)
    private LocalDate customUpdatedAt = LocalDate.now();

    @Column(nullable = false)
    private LocalDate imacUpdatedAt = LocalDate.now();

    // 얘랑 비교해서 똑같은게 최신!
    // 근데 또 compareTo를 잘 쓰면 그냥 바로 비교해버려도 될 것 같기도..
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt = LocalDate.now();

    public void setCustomLocation(final String customLocation) {
        this.customLocation = customLocation;
        this.customUpdatedAt = LocalDate.now();
    }

    public void setImacLocation(final String imacLocation) {
        this.imacLocation = imacLocation;
        this.imacUpdatedAt = LocalDate.now();
    }
}
