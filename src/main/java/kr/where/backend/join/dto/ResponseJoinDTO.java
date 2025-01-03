package kr.where.backend.join.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseJoinDTO {
    private Integer intraId;

    @Builder
    public ResponseJoinDTO(final Integer intraId) {
        this.intraId = intraId;
    }
}
