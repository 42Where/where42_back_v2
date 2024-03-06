package kr.where.backend.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseRefreshTokenDTO {
    private String refreshToken;

    @Builder
    public ResponseRefreshTokenDTO(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
