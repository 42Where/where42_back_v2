package kr.where.backend.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import kr.where.backend.api.dto.OAuthToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime createdAt;

    public Token(final String name, final OAuthToken oAuthToken) {
        this.name = name;
        this.accessToken = oAuthToken.getAccess_token();
        this.refreshToken = oAuthToken.getRefresh_token();

        final LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(oAuthToken.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = localDateTime;
    }

    public void updateToken(final OAuthToken oAuthToken) {
        this.accessToken = oAuthToken.getAccess_token();
        this.refreshToken = oAuthToken.getRefresh_token();

        final LocalDateTime localDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(oAuthToken.getCreated_at()),
                        TimeZone.getDefault().toZoneId());
        this.createdAt = localDateTime;
    }
}
