package kr.where.backend.version;

import kr.where.backend.version.exception.VersionException;

public enum OsType {
    IOS, ANDROID;

    public static void checkAllowedOs(final String os) {
        try {
            OsType.valueOf(os.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VersionException.NotAllowedOsException();
        }
    }
}