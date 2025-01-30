package kr.where.backend.analytics.imacUsageAnalytics;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("SELECT ROW_NUMBER() OVER (ORDER BY imac) AS id, " +
        "imac," +
        "CAST(SUM(EXTRACT(EPOCH FROM logout_at - login_at)) AS BIGINT) AS usage_time, " +
        "COUNT(imac) as usage_count, " +
        "MIN(login_at) as login_at_first, " +
        "MAX(logout_at) as logout_at_last " +
        "FROM imac_history " +
        "WHERE created_at BETWEEN DATE_TRUNC('week', CURRENT_TIMESTAMP - INTERVAL '7' DAY) AND " +
        "DATE_TRUNC('week', CURRENT_TIMESTAMP - INTERVAL '7' DAY) + INTERVAL '6' DAY " +
        "GROUP BY imac"
)
@Table(name = "imac_usage_analytics_view")
@Getter
public class ImacUsageAnalyticsView {
    @Id
    private Long id;
    private String imac;
    private Long usageTime;
    private Long usageCount;
    private LocalDateTime loginAtFirst;
    private LocalDateTime logoutAtLast;
}
