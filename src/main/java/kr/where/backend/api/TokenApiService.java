package kr.where.backend.api;

import kr.where.backend.api.http.HttpRequest;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.OAuthToken;
import kr.where.backend.member.DTO.Seoul42;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenApiService {

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
    @Retryable
    public OAuthToken getOAuthToken(final String secret, final String code) {
        return JsonMapper
                .mapping(HttpResponse.responseBodyOfPost(HttpRequest.requestToken(secret, code), UriBuilder.token()),
                        OAuthToken.class);
    }

    /**
     * refreshToken 으로 intra 에 oAuth token 발급 요청 후 토큰 반환
     */
    @Retryable
    public OAuthToken getOAuthTokenWithRefreshToken(final String secret, final String refreshToken) {
        return JsonMapper
                .mapping(HttpResponse.responseBodyOfPost(
                                HttpRequest.requestAccessToken(secret, refreshToken), UriBuilder.token()),
                        OAuthToken.class);
    }
}
