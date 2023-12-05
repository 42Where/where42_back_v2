package kr.where.backend.auth.exception;

import kr.where.backend.exception.customExceptionBysuhwpark.CustomException;
import kr.where.backend.exception.customExceptionBysuhwpark.ErrorCode;

public class TokenExceptions extends CustomException {

    public TokenExceptions(final ErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidedTokenException extends TokenExceptions{
        public InvalidedTokenException() {
            super(ErrorCode.INVALIDED_TOKEN);
        }
    }

    public static class WrongSignedToken extends TokenExceptions {
        public WrongSignedToken() {
            super(ErrorCode.WRONG_SIGNED_TOKEN);
        }
    }

    public static class ExpiredTokenTimeOutException extends TokenExceptions {
        public ExpiredTokenTimeOutException() {
            super(ErrorCode.EXPIRED_TOKEN_TIME_OUT);
        }
    }

    public static class UnsupportedTokenException extends TokenExceptions {
        public UnsupportedTokenException() {
            super(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public static class IllegalTokenException extends TokenExceptions {
        public IllegalTokenException() {
            super(ErrorCode.INVALIDED_TOKEN);
        }
    }
}
