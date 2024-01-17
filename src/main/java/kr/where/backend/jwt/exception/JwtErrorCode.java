package kr.where.backend.jwt.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtErrorCode implements ErrorCode {
    INVALID_TOKEN(1500,"유효한 Jwt 토큰이 없습니다."),
    WRONG_SIGNED_TOKEN(1501, "서명이 잘못된 Jwt 토큰입니다."),
    EXPIRED_TOKEN_TIME_OUT(1502, "만료된 Jwt 토큰입니다."),
    UNSUPPORTED_TOKEN(1503, "지원 되지 않는 Jwt 토큰입니다."),
    ILLEGAL_TOKEN(1504, "잘못된 Jwt 토큰입니다."),
    UNMATCHED_IP(1505, "발급 시 IP와 다릅니다");

    private final int errorCode;
    private final String errorMessage;
}
