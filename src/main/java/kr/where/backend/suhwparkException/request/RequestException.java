package kr.where.backend.suhwparkException.request;

import java.util.HashSet;
import kr.where.backend.suhwparkException.CustomException;
import kr.where.backend.suhwparkException.ErrorCode;

public class RequestException extends CustomException {
    public RequestException(final RequestErrorCode requestErrorCode) {
        super(requestErrorCode);
    }

    public static class TooManyRequestException extends RequestException{
        public TooManyRequestException() {
            super(RequestErrorCode.TOO_MANY_REQUEST);
        }
    }

    public static class HaneRequestException extends RequestException {
        public HaneRequestException(RequestErrorCode requestErrorCode) {
            super(RequestErrorCode.HANE_SERVICE);
        }
    }

    public static class WhiteLabelException extends RequestException {
        public WhiteLabelException() {
            super(RequestErrorCode.WHITE_LABEL_PAGE);
        }
    }

    public static class BadRequestException extends RequestException {
        public BadRequestException() {
            super(RequestErrorCode.BAD_REQUEST);
        }
    }
}
