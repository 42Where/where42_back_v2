package kr.where.backend.location.dto;

import kr.where.backend.location.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ResponseLocationDTO {
    private Integer intraId;
    private String imacLocation;
    private LocalDateTime imacUpdatedAt;
    private String customLocation;
    private LocalDateTime customUpdatedAt;

    @Builder
    public ResponseLocationDTO(final Location location) {
        this.intraId = location.getMember().getIntraId();
        this.imacLocation = location.getImacLocation();
        this.imacUpdatedAt = location.getImacUpdatedAt();
        this.customLocation = location.getCustomLocation();
        this.customUpdatedAt = location.getCustomUpdatedAt();
    }
}
