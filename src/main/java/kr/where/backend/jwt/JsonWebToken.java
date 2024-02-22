package kr.where.backend.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.where.backend.jwt.exception.JwtException;
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
    private String requestIp;
    private String refreshToken;


    public JsonWebToken(final Integer intraId, final String requestIp, final String refreshToken) {
        this.intraId = intraId;
        this.requestIp = requestIp;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
