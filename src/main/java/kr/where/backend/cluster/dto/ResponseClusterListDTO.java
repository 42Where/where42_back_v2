package kr.where.backend.cluster.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClusterListDTO {
    private List<ResponseClusterDTO> members;

    public static ResponseClusterListDTO of(List<ResponseClusterDTO> members) {
        return new ResponseClusterListDTO(members);
    }
}
