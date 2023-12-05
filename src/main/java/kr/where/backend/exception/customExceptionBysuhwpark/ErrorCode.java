package kr.where.backend.exception.customExceptionBysuhwpark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //token Exception
    INVALIDED_TOKEN(1000,"유효한 토큰이 없습니다"),
    WRONG_SIGNED_TOKEN(1001, "서명이 잘못된 토큰입니다"),
    EXPIRED_TOKEN_TIME_OUT(1002, "만료된 토큰입니다"),
    UNSUPPORTED_TOKEN(1003, "지원 되지 않는 토큰입니다"),
    ILLEGAL_TOKEN(1004, "잘못된 토큰입니다");

    private final int code;
    private final String message;
}
