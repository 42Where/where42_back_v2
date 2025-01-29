package kr.where.backend.analytics.memberImacUsageAnalytics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("SELECT ROW_NUMBER() OVER (ORDER BY imac) AS id, " +
        "intra_id, " +
        "imac, " +
        "CAST(SUM(EXTRACT(EPOCH FROM logout_at - login_at)) AS BIGINT) AS usage_time, " +
        "COUNT(*) AS usage_count, " +
        "MIN(login_at) AS login_at_first, " +
        "MAX(logout_at) AS logout_at_last " +
        "FROM imac_history " +
        "WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '7' DAY " +
        "GROUP BY imac, intra_id")
@Table(name = "member_imac_usage_analytics_view")
@Getter
public class MemberImacUsageAnalyticsView {
    @Id
    private Long id;

    private Integer intraId;

    private String imac;

    private Long usageTime;

    private Long usageCount;

    private LocalDateTime loginAtFirst;

    private LocalDateTime logoutAtLast;
}
