package kr.where.backend.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestAdminStatusDTO {
    private final String role;
}
