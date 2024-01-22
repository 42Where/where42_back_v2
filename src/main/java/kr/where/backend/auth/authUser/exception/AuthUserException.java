package kr.where.backend.auth.authUser.exception;

import kr.where.backend.exception.CustomException;

public class AuthUserException extends CustomException {
    public AuthUserException(final AuthUserErrorCode authUserErrorCode) {
        super(authUserErrorCode);
    }

    public static class ForbiddenUserException extends AuthUserException {
        public ForbiddenUserException() {
            super(AuthUserErrorCode.FORBIDDEN_USER);
        }
    }
}
