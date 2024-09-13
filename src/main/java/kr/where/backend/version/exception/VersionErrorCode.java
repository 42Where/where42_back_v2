package kr.where.backend.version.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VersionErrorCode implements ErrorCode {

    NOT_ALLOWED_OS(1800, "허용되지 않은 OS입니다."),
    INVALID_VERSION_FORMAT(1801, "유효하지 않은 버전 형식 입니다."),
    REQUIRE_VERSION_UPGRADE(1802, "업그레이드가 필요한 버전입니다.");


    private final int errorCode;
    private final String errorMessage;
}
