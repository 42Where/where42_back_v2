package kr.where.backend.analytics.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResponseSeatHistoryListDTO {

    private List<ResponseSeatHistoryDTO> seats;

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    public static class ResponseSeatHistoryDTO {
        private String seat;
        private Integer usingTimeHour;
        private Integer usingTimeMinute;
        private Integer usingTimeSecond;
        private Integer usingCount;
    }
}
