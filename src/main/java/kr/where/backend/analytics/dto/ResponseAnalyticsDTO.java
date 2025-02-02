package kr.where.backend.analytics.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseAnalyticsDTO {
    private List<?> seats;

    public ResponseAnalyticsDTO(final List<?> seats) {
        this.seats = seats;
    }
}
