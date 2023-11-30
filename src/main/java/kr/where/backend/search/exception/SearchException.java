package kr.where.backend.search.exception;

import kr.where.backend.exception.CustomException;

public class SearchException extends CustomException {
    public SearchException(final SearchErrorCode searchErrorCode) {
        super(searchErrorCode);
    }

    public static class InvalidContextException extends SearchException {
        public InvalidContextException() {
            super(SearchErrorCode.INVALID_CONTEXT);
        }
    }

    public static class InvalidLengthException extends SearchException {
        public InvalidLengthException() {
            super(SearchErrorCode.INVALID_LENGTH);
        }
    }
}
