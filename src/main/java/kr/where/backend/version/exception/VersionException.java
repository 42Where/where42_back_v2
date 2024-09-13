package kr.where.backend.version.exception;

import kr.where.backend.exception.CustomException;

public class VersionException extends CustomException {

    public VersionException(final VersionErrorCode versionErrorCode) { super(versionErrorCode); }

    //400
    public static class NotAllowedOsException extends VersionException {
        public NotAllowedOsException() { super(VersionErrorCode.NOT_ALLOWED_OS); }
    }

    //400
    public static class InvalidVersionFormatException extends VersionException {
        public InvalidVersionFormatException() { super(VersionErrorCode.INVALID_VERSION_FORMAT); }
    }

    //426
    public static class RequireVersionUpgradeException extends VersionException {
        public RequireVersionUpgradeException() { super(VersionErrorCode.REQUIRE_VERSION_UPGRADE); }
    }
}
