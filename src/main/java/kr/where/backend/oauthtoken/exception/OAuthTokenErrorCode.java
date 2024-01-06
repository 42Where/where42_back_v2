package kr.where.backend.oauthtoken.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuthTokenErrorCode implements ErrorCode {
    // oauth token
    // (jwt 와 분리하는거 어떨까요?
    // api request exception 도 oauth token 사용하는거니까 3000대나 4000대로 쭉 통일하고요!)
    INVALID_OAUTH_TOKEN(1400,"유효한 OAuth 토큰이 없습니다."),
    INVALID_OAUTH_TOKEN_NAME(1401, "유요하지 않은 OAuth 토큰 이름입니다."),
    DUPLICATED_OAUTH_TOKEN_NAME(1402, "이미 등록된 OAuth 토큰입니다.");

    private final int errorCode;
    private final String errorMessage;
}
