package kr.where.backend.cluster.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClusterErrorCode implements ErrorCode {
    INVALID_PATH_VARIABLE(2100, "유효하지 않은 path variable 입니다.");

    private final int errorCode;
    private final String errorMessage;
}
