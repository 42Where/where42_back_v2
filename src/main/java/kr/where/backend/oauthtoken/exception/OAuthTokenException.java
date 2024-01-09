package kr.where.backend.oauthtoken.exception;

import kr.where.backend.exception.CustomException;

public class OAuthTokenException extends CustomException {

    public OAuthTokenException(final OAuthTokenErrorCode errorCode) {
        super(errorCode);
    }

    // oauth token
    public static class InvalidOAuthTokenException extends OAuthTokenException {
        public InvalidOAuthTokenException() {
            super(OAuthTokenErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    public static class InvalidTokenNameException extends OAuthTokenException {
        public InvalidTokenNameException() {
            super(OAuthTokenErrorCode.INVALID_OAUTH_TOKEN_NAME);
        }
    }
    public static class DuplicatedTokenNameException extends OAuthTokenException {
        public DuplicatedTokenNameException() {
            super(OAuthTokenErrorCode.DUPLICATED_OAUTH_TOKEN_NAME);
        }
    }
}
