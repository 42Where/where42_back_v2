package kr.where.backend.api.exception;

import kr.where.backend.exception.CustomException;

public class RequestException extends CustomException {

    public RequestException(final RequestErrorCode requestErrorCode) {
        super(requestErrorCode);
    }

    public static class ApiUnauthorizedException extends RequestException{
        public ApiUnauthorizedException() {
            super(RequestErrorCode.UNAUTHORIZED);
        }
    }

    public static class TooManyRequestException extends RequestException{
        public TooManyRequestException() {
            super(RequestErrorCode.TOO_MANY_REQUEST);
        }
    }

    public static class BadRequestException extends RequestException {
        public BadRequestException() {
            super(RequestErrorCode.BAD_REQUEST);
        }
    }

    public static class ApiServerErrorException extends RequestException{
        public ApiServerErrorException() {
            super(RequestErrorCode.SERVER_ERROR);
        }
    }
}
