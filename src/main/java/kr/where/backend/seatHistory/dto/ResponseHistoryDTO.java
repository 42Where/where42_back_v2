package kr.where.backend.seatHistory.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseHistoryDTO {
    private String imac;
    private double percent;

    public ResponseHistoryDTO(final String imac, final double percent) {
        this.imac = imac;
        this.percent = percent;
    }
}
