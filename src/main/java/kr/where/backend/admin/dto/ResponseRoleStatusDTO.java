package kr.where.backend.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseRoleStatusDTO {
    private final String intraName;
    private final String role;

    public static ResponseRoleStatusDTO of(final String intraName, final String role) {
        return new ResponseRoleStatusDTO(intraName, role);
    }
}
