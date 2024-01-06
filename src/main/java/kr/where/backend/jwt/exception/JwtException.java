package kr.where.backend.jwt.exception;

import kr.where.backend.exception.CustomException;

public class JwtException extends CustomException {

    public JwtException(JwtErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidJwtToken extends JwtException {
        public InvalidJwtToken() {
            super(JwtErrorCode.INVALIDED_TOKEN);
        }
    }

    public static class WrongSignedJwtToken extends JwtException {
        public WrongSignedJwtToken() {
            super(JwtErrorCode.WRONG_SIGNED_TOKEN);
        }
    }

    public static class ExpiredJwtToken extends JwtException {
        public ExpiredJwtToken() {
            super(JwtErrorCode.EXPIRED_TOKEN_TIME_OUT);
        }
    }

    public static class UnsupportedJwtToken extends JwtException {
        public UnsupportedJwtToken() {
            super(JwtErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public static class IllegalJwtToken extends JwtException {
        public IllegalJwtToken() {
            super(JwtErrorCode.ILLEGAL_TOKEN);
        }
    }
}
