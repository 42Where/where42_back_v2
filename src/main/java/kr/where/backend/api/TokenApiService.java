package kr.where.backend.api;

import kr.where.backend.api.http.HttpHeaderBuilder;
import kr.where.backend.api.http.HttpBodyBuilder;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.OAuthToken;
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
    public OAuthToken fallback(final RuntimeException exception) {
        log.info("[TokenApiService] OAuthToken {}", exception.getMessage());
        throw new RuntimeException();
    }

    /**
     * intra 에 oAuth token 발급 요청 후 토큰을 반환
     */
    @Retryable
    public OAuthToken getOAuthToken(final String code) {
        return JsonMapper
                .mapping(HttpBodyBuilder.responseBodyOfPost(HttpHeaderBuilder.requestToken(code), UriBuilder.token()),
                        OAuthToken.class);
    }

    /**
     * refreshToken 으로 intra 에 oAuth token 발급 요청 후 토큰 반환
     */
    @Retryable
    public OAuthToken getOAuthTokenWithRefreshToken(final String refreshToken) {
        return JsonMapper
                .mapping(HttpBodyBuilder.responseBodyOfPost(
                                HttpHeaderBuilder.requestAccessToken(refreshToken), UriBuilder.token()),
                        OAuthToken.class);
    }
}
