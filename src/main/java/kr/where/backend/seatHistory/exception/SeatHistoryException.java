package kr.where.backend.seatHistory.exception;

import kr.where.backend.exception.CustomException;

public class SeatHistoryException extends CustomException {
    public SeatHistoryException(final SeatHistoryErrorCode errorCode) {
        super(errorCode);
    }

    public static class NoSeatHistoryException extends SeatHistoryException{
        public NoSeatHistoryException() {
            super(SeatHistoryErrorCode.NO_SEAT_HISTORY);
        }
    }
}
