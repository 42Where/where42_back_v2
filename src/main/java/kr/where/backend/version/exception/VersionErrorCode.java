package kr.where.backend.version.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VersionErrorCode implements ErrorCode {

    NOT_ALLOWED_OS(9000, "허용되지 않은 OS입니다."),
    INVALID_VERSION_FORMAT(9001, "유효하지 않은 버전 포맷 입니다."),
    INVALID_VERSION_VALUE(9002, "유효하지 않은 낮은 버전 입니다.");

    private final int errorCode;
    private final String errorMessage;
}
