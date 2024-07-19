package kr.where.backend.api.json.hane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaneResponseDto {
    private String login;
    private String inoutState;

    @Override
    public String toString() {
        return "[hane] : login : " + this.login + ", inOrOutState : " + inoutState;
    }
}
