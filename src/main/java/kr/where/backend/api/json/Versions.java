package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Versions {
    private String small;

    //test
    public static Versions create(String small) {
        Versions versions = new Versions();

        versions.small = small;

        return versions;
    }
}
