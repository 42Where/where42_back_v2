package kr.where.backend.version.exception;

import kr.where.backend.exception.CustomException;

public class VersionException extends CustomException {

    public VersionException(final VersionErrorCode versionErrorCode) { super(versionErrorCode); }

    public static class NotAllowedOsException extends VersionException {
        public NotAllowedOsException() { super(VersionErrorCode.NOT_ALLOWED_OS); }
    }

    public static class InvalidVersionFormatException extends VersionException {
        public InvalidVersionFormatException() { super(VersionErrorCode.INVALID_VERSION_FORMAT); }
    }

    public static class InvalidVersionValueException extends VersionException {
        public InvalidVersionValueException() { super(VersionErrorCode.INVALID_VERSION_VALUE); }
    }
}
