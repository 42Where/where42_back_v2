package kr.where.backend.location.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ResponseClusterUsageListDTO {
    private List<ResponseClusterUsageDTO> clusters;

    private ResponseClusterUsageListDTO(final List<ResponseClusterUsageDTO> clusters) {
        this.clusters = clusters;
    }

    static public ResponseClusterUsageListDTO of (final List<ResponseClusterUsageDTO> clusters) {
        return (new ResponseClusterUsageListDTO(clusters));
    }

}
