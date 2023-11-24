package kr.where.backend.search.exception;

import kr.where.backend.exception.CustomException;
import kr.where.backend.exception.ErrorCode;

public class SearchException extends CustomException {
    public SearchException(final SearchErrorCode searchErrorCode) {
        super(searchErrorCode);
    }

    public static class InvalidKeyWordException extends SearchException {
        public InvalidKeyWordException() {
            super(SearchErrorCode.INVALID);
        }
    }
}
