package kr.where.backend.imacHistory.exception;

import kr.where.backend.exception.CustomException;

public class ImacHistoryException extends CustomException {
    public ImacHistoryException(final ImacHistoryErrorCode errorCode) {
        super(errorCode);
    }

    public static class NoImacHistoryException extends ImacHistoryException {
        public NoImacHistoryException() {
            super(ImacHistoryErrorCode.NO_IMAC_HISTORY);
        }
    }
}
