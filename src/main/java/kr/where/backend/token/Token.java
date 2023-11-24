package kr.where.backend.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import kr.where.backend.api.mappingDto.OAuthToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
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

    public Token(final String name, final OAuthToken oAuthToken) {
        this.name = name;
        this.accessToken = oAuthToken.getAccess_token();
        this.refreshToken = oAuthToken.getRefresh_token();

        final LocalDateTime createdAt =
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(oAuthToken.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

    public Token(final String name, final String accessToken) {
        this.name = name;
        this.accessToken = accessToken;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToken(final OAuthToken oAuthToken) {
        this.accessToken = oAuthToken.getAccess_token();
        this.refreshToken = oAuthToken.getRefresh_token();

        final LocalDateTime createdAt =
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(oAuthToken.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToken(final String accessToken) {
        this.accessToken = accessToken;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isTimeOver() {
        final LocalDateTime currentTime = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        final Duration duration = Duration.between(currentTime, createdAt);
        final Long minute = Math.abs(duration.toMinutes());
        log.info("[accessToken] {} Token 이 발급된지 {}분 지났습니다.", name, minute);
        if (minute > TOKEN_EXPIRATION_MINUTES) {
            return true;
        }
        return false;
    }
}
