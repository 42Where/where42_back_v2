package kr.where.backend.api.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpRequest {
    private static final String BEARER = "Bearer ";
    private static final String CONTENT_VALUES = "application/json;charset=utf-8";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "id"; // 환경변수
    private static final String SECRET = "secret"; // 환경변수
    private static final String REDIRECT_UIR = "callbackAddress";

    public static HttpEntity<MultiValueMap<String, String>> requestToken(final String code) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", SECRET);
        params.add("code", code);
        params.add("redirect_uri", REDIRECT_UIR);

        return new HttpEntity<>(headers, params);
    }

    public static HttpEntity<MultiValueMap<String, String>> requestAccessToken(final String refreshToken) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", SECRET);
        params.add("refresh_token", refreshToken);

        return new HttpEntity<>(headers, params);

    }
    public static HttpEntity<MultiValueMap<String, String>> requestInfo(final String token) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        return new HttpEntity<>(headers, params);
    }
}
