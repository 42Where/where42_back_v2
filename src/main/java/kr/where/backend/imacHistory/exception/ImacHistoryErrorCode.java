package kr.where.backend.imacHistory.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImacHistoryErrorCode implements ErrorCode {
    NO_IMAC_HISTORY(2000, "imac 사용한 기록이 없습니다.");

    private final int errorCode;
    private final String errorMessage;
}
