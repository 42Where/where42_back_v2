package kr.where.backend.cluster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseMostPopularSeatDTO {
    private List<String> imacLocations;

    public static ResponseMostPopularSeatDTO of (List<String> imacLocations) {
//        validateLocation(imacLocations);
        return new ResponseMostPopularSeatDTO(imacLocations);
    }
}
