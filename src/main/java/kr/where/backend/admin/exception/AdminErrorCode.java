package kr.where.backend.admin.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    PERMISSION_DENIED(2000, "권한이 없습니다.");

    private final int errorCode;
    private final String errorMessage;
}
