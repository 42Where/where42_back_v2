package kr.where.backend.admin.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseRoleStatusListDTO {
    private final List<ResponseRoleStatusDTO> roleStatuses;

    public static ResponseRoleStatusListDTO of(final List<ResponseRoleStatusDTO> roleStatuses) {
        return new ResponseRoleStatusListDTO(roleStatuses);
    }
}
