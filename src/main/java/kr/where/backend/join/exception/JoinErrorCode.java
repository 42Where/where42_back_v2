package kr.where.backend.join.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JoinErrorCode implements ErrorCode {
    DUPLICATED_JOIN(1700, "이미 동의한 유저입니다");

    private final int errorCode;
    private final String errorMessage;
}
