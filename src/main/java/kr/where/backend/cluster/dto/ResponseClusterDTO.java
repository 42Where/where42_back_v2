package kr.where.backend.cluster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseClusterDTO {
    private final int memberId;
    private final String intraName;
    private final String image;
    private final String cluster;
    private final int row;
    private final int seat;
    private final Boolean isFriend;
}
