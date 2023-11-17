package kr.where.backend.suhwparkException.token;

import kr.where.backend.suhwparkException.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCode {
    INVALIDED_TOKEN(3000,"유효한 토큰이 없습니다."),
    WRONG_SIGNED_TOKEN(3001, "서명이 잘못된 토큰입니다."),
    EXPIRED_TOKEN_TIME_OUT(3002, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(3003, "지원 되지 않는 토큰입니다."),
    ILLEGAL_TOKEN(3004, "잘못된 토큰입니다.");

    private final int errorCode;
    private final String errorMessage;
}
