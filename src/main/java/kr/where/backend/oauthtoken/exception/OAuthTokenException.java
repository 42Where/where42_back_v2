package kr.where.backend.oauthtoken.exception;

import kr.where.backend.exception.CustomException;

public class OAuthTokenException extends CustomException {

    public OAuthTokenException(final OAuthTokenErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidedOAuthTokenException extends OAuthTokenException {
        public InvalidedOAuthTokenException() {
            super(OAuthTokenErrorCode.INVALIDED_TOKEN);
        }
    }

    public static class WrongSignedOAuthToken extends OAuthTokenException {
        public WrongSignedOAuthToken() {
            super(OAuthTokenErrorCode.WRONG_SIGNED_TOKEN);
        }
    }

    public static class ExpiredOAuthTokenTimeOutException extends OAuthTokenException {
        public ExpiredOAuthTokenTimeOutException() {
            super(OAuthTokenErrorCode.EXPIRED_TOKEN_TIME_OUT);
        }
    }

    public static class UnsupportedOAuthTokenException extends OAuthTokenException {
        public UnsupportedOAuthTokenException() {
            super(OAuthTokenErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public static class IllegalOAuthTokenException extends OAuthTokenException {
        public IllegalOAuthTokenException() {
            super(OAuthTokenErrorCode.INVALIDED_TOKEN);
        }
    }
}
