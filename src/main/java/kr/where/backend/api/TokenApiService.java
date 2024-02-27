package kr.where.backend.api;

import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.json.OAuthTokenDto;
import kr.where.backend.api.exception.RequestException;
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
     * intra 에 oAuth token 발급 요청 후 토큰을 반환
     */
    @Retryable
    public OAuthTokenDto getOAuthToken(final String code) {
        return JsonMapper
                .mapping(HttpResponse.postMethod(HttpHeader.requestToken(code), UriBuilder.token()),
                        OAuthTokenDto.class);
    }

    /**
     * refreshToken 으로 intra 에 oAuth token 발급 요청 후 토큰 반환
     */
    @Retryable
    public OAuthTokenDto getOAuthTokenWithRefreshToken(final String refreshToken) {
        return JsonMapper
                .mapping(HttpResponse.postMethod(
                                HttpHeader.requestAccessToken(refreshToken), UriBuilder.token()),
                        OAuthTokenDto.class);
    }

    /**
     * 요청 3번 실패 시 실행되는 메서드
     */
    @Recover
    public OAuthTokenDto fallback(final RuntimeException exception) {
        log.info("[TokenApiService] OAuthToken {}", exception.getMessage());
        throw new RequestException.BadRequestException();
    }
}
