package kr.where.backend.auth.filter;

public enum JwtConstants {
    HEADER_TYPE("Bearer"),
    ACCESS("accessToken"),
    REFRESH("refreshToken"),
    USER_SUBJECTS("User"),
    USER_ID("intraId"),
    USER_NAME("intraName"),
    USER_ROLE("Cadet"),
    TOKEN_TYPE("type"),
    ROLE_LEVEL("role"),
    REISSUE_URI("/v3/jwt/reissue");

    private final String value;

    JwtConstants(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
