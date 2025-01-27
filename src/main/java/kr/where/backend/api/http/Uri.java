package kr.where.backend.api.http;

public enum Uri {
    HTTPS("https"),
    HOST("api.intra.42.fr"),
    TOKEN_PATH("oauth/token"),
    ME_PATH("v2/me"),
    CADET_PATH("v2/users/"),
    USERS_PATH("v2/campus/29/users"),
    LOCATIONS_PATH("v2/campus/29/locations"),
    HANE_PATH("https://api.24hoursarenotenough.42seoul.kr/ext/where42/where42/"),
    SORT("sort"),
    FILTER_KIND("filter[kind]"),
    FILTER_ACTIVE("filter[active]"),
    PAGE_SIZE("page[size]"),
    PAGE_NUMBER("page[number]"),
    RANGE_BEGIN("range[begin_at]"),
    RANGE_END("range[end_at]"),
    RANGE_LOGIN("range[login]"),
    DELIMITER(","),
    HANE_INFO_LIST("where42All");

    private final String value;

    Uri(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
