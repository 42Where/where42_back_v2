package kr.where.backend.api.mappingDto;

import lombok.Getter;

@Getter
public class OAuthToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private int created_at;
    private int secret_valid_until;
}
