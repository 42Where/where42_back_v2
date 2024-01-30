package kr.where.backend.join.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseJoinDTO {
    private String refreshToken;

    @Builder
    public ResponseJoinDTO(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
