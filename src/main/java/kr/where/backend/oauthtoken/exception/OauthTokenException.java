package kr.where.backend.oauthtoken.exception;

import kr.where.backend.exception.CustomException;

public class OauthTokenException extends CustomException {

    public OauthTokenException(final OauthTokenErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidedOauthTokenException extends OauthTokenException {
        public InvalidedOauthTokenException() {
            super(OauthTokenErrorCode.INVALIDED_TOKEN);
        }
    }

    public static class WrongSignedOauthToken extends OauthTokenException {
        public WrongSignedOauthToken() {
            super(OauthTokenErrorCode.WRONG_SIGNED_TOKEN);
        }
    }

    public static class ExpiredOauthTokenTimeOutException extends OauthTokenException {
        public ExpiredOauthTokenTimeOutException() {
            super(OauthTokenErrorCode.EXPIRED_TOKEN_TIME_OUT);
        }
    }

    public static class UnsupportedOauthTokenException extends OauthTokenException {
        public UnsupportedOauthTokenException() {
            super(OauthTokenErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public static class IllegalOauthTokenException extends OauthTokenException {
        public IllegalOauthTokenException() {
            super(OauthTokenErrorCode.INVALIDED_TOKEN);
        }
    }
}
