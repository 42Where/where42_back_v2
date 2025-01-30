package kr.where.backend.analytics.cache;

import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsRepository;
import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsView;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsRepository;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsView;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsCacheService {
    private final MemberImacUsageAnalyticsRepository memberImacUsageAnalyticsRepository;
    private final ImacUsageAnalyticsRepository imacUsageAnalyticsRepository;

    @Cacheable(key = "#intraId", value = "analyticsCache", cacheManager = "redisCacheManager", unless = "#result.isEmpty()")
    public List<MemberImacUsageAnalyticsView> getMemberImacUsageAnalyticsCacheData(final Integer intraId) {
        return memberImacUsageAnalyticsRepository.findAllBy(intraId);
    }

    @Cacheable(key = "'imacHistory'", value = "analyticsCache", cacheManager = "redisCacheManager", unless = "#result.isEmpty()")
    public List<ImacUsageAnalyticsView> getImacUsageAnalyticsCacheData() {
        return imacUsageAnalyticsRepository.findAllOrOrderByUsageCount();
    }
}
