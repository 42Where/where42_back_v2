package kr.where.backend.api.json.hane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hane {
    private String inoutState;

    // test
    public static Hane create(String inoutState) {
        Hane hane = new Hane();

        hane.inoutState = inoutState;

        return hane;
    }
}