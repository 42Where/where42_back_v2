package kr.where.backend.api.mappingDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Versions {
    private String small;

    //test
    public static Versions createForTest(String small) {
        Versions versions = new Versions();

        versions.small = small;

        return versions;
    }
}
