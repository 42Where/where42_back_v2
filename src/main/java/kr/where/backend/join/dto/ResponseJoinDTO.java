package kr.where.backend.join.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseJoinDTO {
    private String accessToken;

    @Builder
    public ResponseJoinDTO(final String accessToken) {
        this.accessToken = accessToken;
    }
}
