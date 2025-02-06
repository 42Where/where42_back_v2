package kr.where.backend.analytics.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ResponseAnalyticsAbstract {
    protected String seat;
    protected Long usingTimeHour;
    protected Long usingTimeMinute;
    protected Long usingTimeSecond;

    public ResponseAnalyticsAbstract(final String seat, final Long usageTime) {
        this.seat = seat;
        this.usingTimeHour = usageTime / 3600; // 1시간 = 3600초
        this.usingTimeMinute = (usageTime % 3600) / 60; // 남은 초에서 분 계산
        this.usingTimeSecond = (usageTime) % 60; // 남은 초 계산
    }
}
