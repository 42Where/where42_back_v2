package kr.where.backend.location;

import jakarta.persistence.*;
import kr.where.backend.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

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
    private LocalDateTime customUpdatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime imacUpdatedAt = LocalDateTime.now();

    public void setCustomLocation(final String customLocation) {
        this.customLocation = customLocation;
        this.customUpdatedAt = LocalDateTime.now();
    }

    public void setImacLocation(final String imacLocation) {
        this.imacLocation = imacLocation;
        this.imacUpdatedAt = LocalDateTime.now();
    }

    public String getLocation(final String member) {
        if (member == null)
            throw new RuntimeException("멤버가 없습니다");

        if (!getMember().isAgree()) {
            return this.imacLocation;
        } else {
            if (customLocation.isEmpty() && imacLocation.isEmpty()) {
                return null;
            } else if (customLocation.isEmpty()) {
                return imacLocation;
            } else if (imacLocation.isEmpty()) {
                return customLocation;
            } else {
                if (customUpdatedAt.isAfter(imacUpdatedAt)) {
                    return customLocation;
                } else
                    return imacLocation;
            }
        }
    }
}
