package kr.where.backend.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseRoleDTO {
    private final String intraName;
    private final String role;

    public static ResponseRoleDTO of(final String intraName, final String role) {
        return new ResponseRoleDTO(intraName, role);
    }
}