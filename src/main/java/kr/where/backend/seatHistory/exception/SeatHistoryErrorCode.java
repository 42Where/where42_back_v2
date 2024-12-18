package kr.where.backend.seatHistory.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatHistoryErrorCode implements ErrorCode {
    NO_SEAT_HISTORY(2000, "사용한 자리 기록이 없습니다.");

    private final int errorCode;
    private final String errorMessage;
}
