package kr.where.backend.announcement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestPaginationDTO {
    @NotNull
    @Min(value = 0)
    private Integer page;
    @NotNull
    @Min(value = 1)
    private Integer size;
}
