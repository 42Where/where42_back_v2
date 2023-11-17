package kr.where.backend.suhwparkException.token;

import kr.where.backend.suhwparkException.CustomException;

public class TokenException extends CustomException {

    public TokenException(final TokenErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidedTokenException extends TokenException {
        public InvalidedTokenException() {
            super(TokenErrorCode.INVALIDED_TOKEN);
        }
    }

    public static class WrongSignedToken extends TokenException {
        public WrongSignedToken() {
            super(TokenErrorCode.WRONG_SIGNED_TOKEN);
        }
    }

    public static class ExpiredTokenTimeOutException extends TokenException {
        public ExpiredTokenTimeOutException() {
            super(TokenErrorCode.EXPIRED_TOKEN_TIME_OUT);
        }
    }

    public static class UnsupportedTokenException extends TokenException {
        public UnsupportedTokenException() {
            super(TokenErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public static class IllegalTokenException extends TokenException {
        public IllegalTokenException() {
            super(TokenErrorCode.INVALIDED_TOKEN);
        }
    }
}
