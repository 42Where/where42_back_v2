package kr.where.backend.oauthtoken;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import kr.where.backend.api.json.OAuthTokenDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthToken {
    private static final int TOKEN_EXPIRATION_MINUTES = 60;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public OAuthToken(final String name, final OAuthTokenDto oAuthTokenDto) {
        this.name = name;
        this.accessToken = oAuthTokenDto.getAccess_token();
        this.refreshToken = oAuthTokenDto.getRefresh_token();

        final LocalDateTime createdAt =
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(oAuthTokenDto.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToken(final OAuthTokenDto oAuthTokenDto) {
        this.accessToken = oAuthTokenDto.getAccess_token();
        this.refreshToken = oAuthTokenDto.getRefresh_token();

        final LocalDateTime createdAt =
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(oAuthTokenDto.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isTimeOver() {
        final LocalDateTime currentTime = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        final Duration duration = Duration.between(currentTime, createdAt);
        final Long minute = Math.abs(duration.toMinutes());
        log.info("[oAuthToken] : {} Token 이 발급된지 {}분 지났습니다.", name, minute);

        return minute > TOKEN_EXPIRATION_MINUTES;
    }
}
