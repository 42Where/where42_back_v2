package kr.where.backend.member.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.where.backend.member.Enum.Planet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "위치 정보")
public class Locate {
    @Enumerated(EnumType.ORDINAL)
    @Schema(description = "서초, 개초 클러스터 상태")
    private Planet planet;
    @Schema(description = "층 정보")
    private int floor;
    @Schema(description = "서초 클러스터")
    private int cluster;
    @Schema(description = "자리정보")
    private String spot;

    public Locate (Planet planet, int floor, int cluster, String spot) {
        this.planet = planet;
        this.floor = floor;
        this.cluster = cluster;
        this.spot = spot;
    }
    public void updatePlanet(Planet planet) {
        this.planet = planet;
    }
    public void updateLocate(Planet planet, int floor, int cluster, String spot) {
        this.planet = planet;
        this.floor = floor;
        this.cluster = cluster;
        this.spot = spot;
    }

    public static Locate parseLocate(String location) {
        if (location == null)
            return new Locate(null, 0, 0, null);
        int i = location.charAt(1) - '0';
        if ((i >= 1 && location.charAt(2) != '0') && i <= 6 || location.charAt(1) == 'x') {
            if (i <= 2)
                return new Locate(Planet.gaepo, 2, 0, location);
            else if (i <= 4)
                return new Locate(Planet.gaepo, 4, 0, location);
            else if (i <= 6)
                return new Locate(Planet.gaepo, 5, 0, location);
            else
                return new Locate(Planet.gaepo, 3, 0, location);
        } else if (i >= 7 && i <= 9)
            return new Locate(Planet.seocho, 0, i, location);
        return new Locate(Planet.seocho, 0, 10, location);
    }
}
