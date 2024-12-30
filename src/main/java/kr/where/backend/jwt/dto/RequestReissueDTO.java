package kr.where.backend.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestReissueDTO {
    private Integer intraId;
}
