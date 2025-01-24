package kr.where.backend.analytics.memberImacUsageAnalytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberImacUsageAnalyticsRepository extends JpaRepository<MemberImacUsageAnalyticsView, Long> {
    @Query("SELECT memberImacData FROM MemberImacUsageAnalyticsView memberImacData " +
            "WHERE memberImacData.intraId =:intraId ORDER BY memberImacData.usageCount DESC")
    List<MemberImacUsageAnalyticsView> findAllBy(@Param(value = "intraId") final Integer intraId);
}
