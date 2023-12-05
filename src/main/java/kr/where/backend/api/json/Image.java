package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    private Versions versions;

    //test
    public static Image createForTest(Versions versions) {
        Image image = new Image();

        image.versions = versions;

        return image;
    }
}
