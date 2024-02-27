package kr.where.backend.auth.authUser.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthUserErrorCode implements ErrorCode {
    FORBIDDEN_USER(1800, "Where42 서비스에 동의한 user가 아닙니다."),
    ANONYMOUS_USER(1801, "인가 인증을 받지 않은 user 입니다");

    private final int errorCode;
    private final String errorMessage;
}
