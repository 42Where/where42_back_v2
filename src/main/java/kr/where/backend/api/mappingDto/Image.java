package kr.where.backend.api.mappingDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.thymeleaf.standard.expression.IStandardConversionService;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    private String link;
    private Versions versions;

    //test
    public static Image createForTest(Versions versions) {
        Image image = new Image();

        image.versions = versions;

        return image;
    }
}
