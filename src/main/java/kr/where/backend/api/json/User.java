package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer id;
    private String login;
    private Image image;
    private String location;
    @JsonProperty("active?")
    private boolean active;
    private String created_at;
}
