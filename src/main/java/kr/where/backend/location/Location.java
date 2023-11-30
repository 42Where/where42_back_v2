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
    @Column(name = "location_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 30)
    private String customLocation;

    @Column(length = 7)
    private String imacLocation;

    private LocalDateTime customUpdatedAt;

    private LocalDateTime imacUpdatedAt;

    public Location(final Member member, final String imacLocation) {
        this.member = member;
        this.imacLocation = imacLocation;
        this.imacUpdatedAt = LocalDateTime.now();
    }

    public Location(final String imacLocation) {
        this.imacLocation = imacLocation;
        this.imacUpdatedAt = LocalDateTime.now();
    }

    public  void setMember(Member member) {
        this.member = member;
    }

    public void setCustomLocation(final String customLocation) {
        this.customLocation = customLocation;
        this.customUpdatedAt = LocalDateTime.now();
    }

    public void setImacLocation(final String imacLocation) {
        this.imacLocation = imacLocation;
        this.imacUpdatedAt = LocalDateTime.now();
    }

    public String getLocation() {
        if (!this.member.isAgree()) {
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
