package kr.where.backend.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.IllegalOAuthTokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JsonWebToken {
    @Id
    @GeneratedValue
    private Long id;
    private Integer intraId;
    private String refreshToken;


    public JsonWebToken(final Integer intraId, final String refreshToken) {
        this.intraId = intraId;
        this.refreshToken = refreshToken;
    }

    public void validateRefreshToken(final String refreshToken) {
        if (!refreshToken.equals(this.refreshToken)) {
            throw new IllegalOAuthTokenException();
        }
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
