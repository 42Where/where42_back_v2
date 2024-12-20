package kr.where.backend.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseCheckAdminDTO {
    private boolean isAdmin;

    static public ResponseCheckAdminDTO of(final boolean isAdmin) {
        return new ResponseCheckAdminDTO(isAdmin);
    }
}
