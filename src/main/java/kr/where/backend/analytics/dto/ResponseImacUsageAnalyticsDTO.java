package kr.where.backend.analytics.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseImacUsageAnalyticsDTO extends ResponseAnalyticsAbstract {
    private Long usingUserCount;

    public ResponseImacUsageAnalyticsDTO(final String imac, final Long usageTime, final Long usageCount) {
        super(imac, usageTime);
        this.usingUserCount = usageCount;
    }
}
