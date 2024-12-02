package kr.where.backend.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRoleStatusDTO {
    @NotBlank
    private String intraName;
    @NotBlank
    private String role;
}
