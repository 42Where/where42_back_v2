package kr.where.backend.imacHistory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "imac_history", indexes = {
        @Index(name = "idx_intraid_imac", columnList = "intra_id, imac")
})
public class ImacHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "intra_id")
    private Integer intraId;

    @Column(name = "imac", length = 10, nullable = false)
    private String imac;

    private LocalDateTime loginAt;

    private LocalDateTime logoutAt;

    private LocalDateTime createdAt;

    public ImacHistory(final Integer intraId, final String imac, final String loginAt, final String logoutAt) {
        this.intraId = intraId;
        this.imac = imac;
        this.loginAt = getKstDateTime(loginAt);
        this.logoutAt = getKstDateTime(logoutAt);
        this.createdAt = LocalDateTime.now();
    } 


    private LocalDateTime getKstDateTime(final String time) {
        final DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

        final ZonedDateTime utc = ZonedDateTime.parse(time, utcFormatter);
        final ZonedDateTime kts = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        return kts.toLocalDateTime().withNano(0);
    }

    public long usedImacTime() {
        return Duration.between(this.loginAt, this.logoutAt).getSeconds();
    }

    //for test
    public void setCreatedAtForTest(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
