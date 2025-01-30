package kr.where.backend.analytics.imacUsageAnalytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImacUsageAnalyticsRepository extends JpaRepository<ImacUsageAnalyticsView, Long> {
    @Query("SELECT imacData FROM ImacUsageAnalyticsView imacData ORDER BY imacData.usageCount DESC, imacData.usageTime DESC")
    List<ImacUsageAnalyticsView> findAllOrOrderByUsageCount();
}
