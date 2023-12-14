package kr.where.backend.oauthtoken.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OauthTokenErrorCode implements ErrorCode {
    INVALIDED_TOKEN(3000,"유효한 토큰이 없습니다."),
    WRONG_SIGNED_TOKEN(3001, "서명이 잘못된 토큰입니다."),
    EXPIRED_TOKEN_TIME_OUT(3002, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(3003, "지원 되지 않는 토큰입니다."),
    ILLEGAL_TOKEN(3004, "잘못된 토큰입니다."),

    INVALID_OAUTH_TOKEN(3005,"유효한 OAuth 토큰이 없습니다."),
    INVALID_OAUTH_TOKEN_NAME(3006, "유요하지 않은 OAuth 토큰 이름입니다."),
    DUPLICATED_OAUTH_TOKEN_NAME(3007, "이미 등록된 OAuth 토큰입니다."),
    ;

    private final int errorCode;
    private final String errorMessage;
}
