package kr.where.backend.cluster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseClusterDTO {
    private Integer intraId;
    private String intraName;
    private String image;
    private String cluster;
    private int row;
    private int seat;
    private Boolean isFriend;

    private static final String LOCATION_SPLIT_REGEX = "r|s";

    public ResponseClusterDTO (final Integer intraId,
                               final String intraName,
                               final String image,
                               final String location,
                               final Boolean isFriend) {
        this.intraId = intraId;
        this.intraName = intraName;
        this.image = image;
        this.isFriend = isFriend;
        setLocation(location);
    }

    private void setLocation(final String location) {
        final String[] parsedLocation = location.split(LOCATION_SPLIT_REGEX);

        this.cluster = parsedLocation[0];
        this.row = Integer.parseInt(parsedLocation[1]);
        this.seat = Integer.parseInt(parsedLocation[2]);
    }
}
