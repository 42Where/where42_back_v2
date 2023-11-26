package kr.where.backend.api.mappingDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    private String link;
    private Versions versions;
}
