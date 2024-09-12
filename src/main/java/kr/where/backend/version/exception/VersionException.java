package kr.where.backend.version.exception;

import kr.where.backend.exception.CustomException;

public class VersionException extends CustomException {

    public VersionException(final VersionErrorCode versionErrorCode) { super(versionErrorCode); }

    public static class NotAllowedOsException extends VersionException {
        public NotAllowedOsException() { super(VersionErrorCode.NOT_ALLOWED_OS); }
    }

    public static class InvalidVersionException extends VersionException {
        public InvalidVersionException() { super(VersionErrorCode.INVALID_VERSION); }
    }
}
