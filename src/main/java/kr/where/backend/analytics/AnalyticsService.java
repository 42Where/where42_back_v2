package kr.where.backend.analytics;

import kr.where.backend.analytics.cache.AnalyticsCacheService;
import kr.where.backend.analytics.dto.ResponseAnalyticsDTO;
import kr.where.backend.analytics.dto.ResponseImacUsageAnalyticsDTO;
import kr.where.backend.analytics.dto.ResponseMemberImacUsageAnalyticsDTO;
import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsRepository;
import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsView;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsRepository;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsView;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    final MemberImacUsageAnalyticsRepository memberImacUsageAnalyticsRepository;
    final ImacUsageAnalyticsRepository imacUsageAnalyticsRepository;
    final AnalyticsCacheService analyticsCacheService;

    public ResponseAnalyticsDTO getMemberImacUsageAnalyticsData(final Integer intraId, final Integer count) {
        final List<MemberImacUsageAnalyticsView> cacheData = analyticsCacheService.getMemberImacUsageAnalyticsCacheData(intraId);

        return new ResponseAnalyticsDTO(cacheData.stream()
                .limit(count)
                .map(cache -> new ResponseMemberImacUsageAnalyticsDTO(
                        cache.getImac(),
                        cache.getUsageTime(),
                        cache.getUsageCount()
                        )
                )
                .toList()
        );
    }

    public ResponseAnalyticsDTO getImacUsageAnalyticsData(final Integer count) {
        final List<ImacUsageAnalyticsView> cacheData = analyticsCacheService.getImacUsageAnalyticsCacheData();

        return new ResponseAnalyticsDTO(cacheData.stream()
                .limit(count)
                .map(cache -> new ResponseImacUsageAnalyticsDTO(
                        cache.getImac(),
                        cache.getUsageTime(),
                        cache.getUsageCount()
                        )
                )
                .toList()
        );
    }
}
