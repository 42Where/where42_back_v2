package kr.where.backend.location.dto;

import kr.where.backend.location.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ResponseLocationDto {
    private Long intraId;
    private String imacLocation;
    private LocalDateTime imacUpdateAt;
    private String customLocation;
    private LocalDateTime customUpdateAt;

    @Builder
    public ResponseLocationDto(final Location location) {
        this.intraId = location.getMember().getIntraId();
        this.imacLocation = location.getImacLocation();
        this.imacUpdateAt = location.getImacUpdatedAt();
        this.customLocation = location.getCustomLocation();
        this.customUpdateAt = location.getCustomUpdatedAt();
    }
}
