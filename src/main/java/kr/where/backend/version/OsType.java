package kr.where.backend.version;

import kr.where.backend.version.exception.VersionException;

public enum OsType {
    IOS, ANDROID;

    public static OsType checkAllowedOs(String os) {
        try {
            return OsType.valueOf(os.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VersionException.NotAllowedOsException();
        }
    }
}