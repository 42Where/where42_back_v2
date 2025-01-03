package kr.where.backend.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestReissueDTO {
    @NotBlank
    private Integer intraId;
}
