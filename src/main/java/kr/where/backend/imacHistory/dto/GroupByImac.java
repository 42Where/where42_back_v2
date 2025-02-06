package kr.where.backend.imacHistory.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GroupByImac {
    private Integer intraId;
    private String imac;
    private Long count;
    private Long usageTime;

    public GroupByImac(final Integer intraId, final String imac, final Long count, final Long usageTime) {
        this.intraId = intraId;
        this.imac = imac;
        this.count = count;
        this.usageTime = usageTime;
    }
}
