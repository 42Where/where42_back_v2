package kr.where.backend.version;

import kr.where.backend.version.exception.VersionException;

public enum OsTypes {
    IOS, ANDROID;

    public static OsTypes checkToExistOs(String os) {
        try {
            return OsTypes.valueOf(os.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VersionException.NotAllowedOsException();
        }
    }
}
