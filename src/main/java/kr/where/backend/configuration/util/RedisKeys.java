package kr.where.backend.configuration.util;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Getter
public enum RedisKeys {
    SEARCH("searchCache"),
    ANALYTICS("analyticsCache");

    private final String name;

    RedisKeys(final String name) {
        this.name = name;
    }

    public static Duration getTtl(final String name) {
        switch (name) {
            case "searchCache" -> {
                return Duration.ofMinutes(3L);
            }
            case "analyticsCache" -> {
                final LocalDateTime now = LocalDateTime.now();
                final LocalDateTime nextSunday = now.toLocalDate()
                        .with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay();
                return Duration.ofSeconds(ChronoUnit.SECONDS.between(now, nextSunday));
            }
        }
        return Duration.ofSeconds(0L);
    }
}
