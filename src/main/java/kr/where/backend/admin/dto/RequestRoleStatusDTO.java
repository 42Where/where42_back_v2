package kr.where.backend.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRoleStatusDTO {
    @NotBlank
    @Size(max = 15)
    private String intraName;
    @NotBlank
    @Size(max = 15)
    private String role;
}
