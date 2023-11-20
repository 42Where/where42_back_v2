package kr.where.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import kr.where.backend.api.dto.OAuthToken;
import kr.where.backend.member.DTO.Seoul42;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenApiService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private HttpEntity<MultiValueMap<String, String>> request;
    private MultiValueMap<String, String> params;
    private ResponseEntity<String> response;

    public HttpEntity<MultiValueMap<String, String>> request42TokenHeader(final String secret, final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "");
        params.add("client_secret", secret);
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/v2/auth/callback");
        return new HttpEntity<>(params, headers);
    }

    public ResponseEntity<String> postResponseApi(final HttpEntity<MultiValueMap<String, String>> request, final URI url) {
        return restTemplate.exchange(
                url.toString(),
                HttpMethod.POST,
                request,
                String.class);
    }

    /**
     * 요청 3번 실패 시 실행되는 메서드
     */
    @Recover
    public Seoul42 fallback(final RuntimeException e, final String token) {
        log.info("[ApiService] {}", e.getMessage());
        throw new RuntimeException();
    }

    /**
     * intra 에 oAuth token 발급 요청 후 토큰을 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public OAuthToken getOAuthToken(final String secret, final String code) {
        request = request42TokenHeader(secret, code);
        response = postResponseApi(request, request42TokenUri());
        return oAuthTokenMapping(response.getBody());
    }

    public URI request42TokenUri() {
        return UriComponentsBuilder.fromHttpUrl("https://api.intra.42.fr/oauth/token")
                .build()
                .toUri();
    }

    public OAuthToken oAuthTokenMapping(final String body) {
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(body, OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(); // checked exception 좀 더 구체적인 exception 을 던져야 함
        }
        return oAuthToken;
    }


    /**
     * refreshToken 으로 intra 에 oAuth token 발급 요청 후 토큰 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public OAuthToken getOAuthTokenWithRefreshToken(final String secret, final String refreshToken) {
        request = request42RefreshHeader(secret, refreshToken);
        response = postResponseApi(request, request42TokenUri());
        return oAuthTokenMapping(response.getBody());
    }

    public HttpEntity<MultiValueMap<String, String>> request42RefreshHeader(final String secret, final String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", "");
        params.add("client_secret", secret);
        params.add("refresh_token", refreshToken);
        return new HttpEntity<>(params, headers);
    }
}
