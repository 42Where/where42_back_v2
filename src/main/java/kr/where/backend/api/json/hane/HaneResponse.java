package kr.where.backend.api.json.hane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaneResponse {
    private String login;
    private String inOrOut;
}
