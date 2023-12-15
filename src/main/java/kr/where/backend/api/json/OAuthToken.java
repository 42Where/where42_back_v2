package kr.where.backend.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private int created_at;
}
