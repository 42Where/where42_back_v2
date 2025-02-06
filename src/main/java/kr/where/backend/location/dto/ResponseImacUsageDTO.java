package kr.where.backend.location.dto;

import lombok.Getter;

@Getter
public class ResponseImacUsageDTO {
    private int usageRate;
    private int usingImacUserCount;
    private int totalUserCount;

    private ResponseImacUsageDTO(final int usageRate, final int usingImacUserCount, final int totalUserCount) {
        this.usageRate = usageRate;
        this.usingImacUserCount = usingImacUserCount;
        this.totalUserCount = totalUserCount;
    }

    static public ResponseImacUsageDTO of (final int usageRate, final int usingImacUserCount, final int totalUserCount) {
        return (new ResponseImacUsageDTO(usageRate, usingImacUserCount, totalUserCount));
    }
}
