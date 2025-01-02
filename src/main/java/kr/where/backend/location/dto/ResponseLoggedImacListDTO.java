package kr.where.backend.location.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLoggedImacListDTO {
    private List<ResponseLoggedImacDTO> members;

    public static ResponseLoggedImacListDTO of(List<ResponseLoggedImacDTO> members) {
        return new ResponseLoggedImacListDTO(members);
    }
}
