package kr.where.backend.api.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpHeader {
    private static final String BEARER = "Bearer ";
    private static final String GRANT_TYPE_ACCESS = "authorization_code";
    private static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static HttpEntity<MultiValueMap<String, String>> requestToken(final String code) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

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

        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

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
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        return new HttpEntity<>(params, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> requestHaneInfo(final String token) {
        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        return new HttpEntity<>(params, headers);
    }
}
