package kr.where.backend.version;

public enum OsTypes {
    IOS, ANDROID;

    public static OsTypes checkToExistOs(String os) {
        try {
            return OsTypes.valueOf(os.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
