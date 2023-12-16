package kr.where.backend.api.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpHeader {
    private static final String BEARER = "Bearer ";
    private static final String INTRA_CONTENT_VALUES = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String HANE_CONTENT_VALUES = "application/json;charset=utf-8";
    private static final String GRANT_TYPE_ACCESS = "authorization_code";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";
//    private static final String CLIENT_ID = "id"; // 환경변수
//    private static final String SECRET = "secret"; // 환경변수
//    private static final String REDIRECT_URI = "callbackAddress";

    public static HttpEntity<MultiValueMap<String, String>> requestToken(final String code) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, INTRA_CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", GRANT_TYPE_ACCESS);
        params.add("client_id", Utils.getClientId());
        params.add("client_secret", Utils.getSecret());
        params.add("code", code);
        params.add("redirect_uri", Utils.getRedirectUri());

        return new HttpEntity<>(params, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> requestAccessToken(final String refreshToken) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, INTRA_CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", GRANT_TYPE_REFRESH);
        params.add("client_id", Utils.getClientId());
        params.add("client_secret", Utils.getSecret());
        params.add("refresh_token", refreshToken);

        return new HttpEntity<>(params, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> request42Info(final String token) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
        headers.add(HttpHeaders.CONTENT_TYPE, INTRA_CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        return new HttpEntity<>(params, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> requestHaneInfo(final String token) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
        headers.add(HttpHeaders.CONTENT_TYPE, HANE_CONTENT_VALUES);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        return new HttpEntity<>(params, headers);
    }
}
