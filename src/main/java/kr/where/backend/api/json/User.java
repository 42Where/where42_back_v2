package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class User {
    private Integer id;
    private String login;
    private Image image;
    @JsonProperty("active?")
    private boolean active;
    private String created_at;
}
