package kr.where.backend.join.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseJoin {
    private String accessToken;

    @Builder
    public ResponseJoin(final String accessToken) {
        this.accessToken = accessToken;
    }
}
