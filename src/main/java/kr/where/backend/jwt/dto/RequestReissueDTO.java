package kr.where.backend.jwt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestReissueDTO {
    @NotNull
    private Integer intraId;
}
