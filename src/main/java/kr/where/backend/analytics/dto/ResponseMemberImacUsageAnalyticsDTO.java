package kr.where.backend.analytics.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseMemberImacUsageAnalyticsDTO extends ResponseAnalyticsAbstract {
    private Long usingCount;

    public ResponseMemberImacUsageAnalyticsDTO(final String imac, final Long usageTime, final Long usageCount) {
        super(imac, usageTime);
        this.usingCount = usageCount;
    }
}
