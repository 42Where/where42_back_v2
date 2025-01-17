package kr.where.backend.analytics.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResponsePopularImacListDTO {
    private List<ResponsePopularImacDTO> seats;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class ResponsePopularImacDTO {
        private String seat;
        private Integer usingTimeHour;
        private Integer usingTimeMinute;
        private Integer usingTimeSecond;
        private Integer usingUserCount;
    }
}
