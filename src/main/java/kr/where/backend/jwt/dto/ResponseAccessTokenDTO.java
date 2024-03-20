package kr.where.backend.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAccessTokenDTO {
    private String accessToken;

    @Builder
    public ResponseAccessTokenDTO(final String accessToken) {
        this.accessToken = accessToken;
    }
}
