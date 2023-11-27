package kr.where.backend.api.mappingDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.thymeleaf.standard.expression.IStandardConversionService;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hane {
    private String login;
    private String inoutState;
    private String cluster;
    private String tag_at;

    // test
    public static Hane createForTest(String inoutState) {
        Hane hane = new Hane();

        hane.inoutState = inoutState;

        return hane;
    }
}