package kr.where.backend.api.http;


import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 하은님 이거 enum으로 관리 가능할거같아요. 한번 확인해보시고 바꾸는게 낫다고 생각되시면 알려주세요~
 */
public class UriBuilder {

    private static final String HTTPS = "https";
    private static final String HOST = "api.intra.42.fr";
    private static final String TOKEN_PATH = "oauth/token";
    private static final String ME_PATH = "v2/me";
    private static final String CADET_PATH = "v2/users/";
    private static final String USERS_PATH = "v2/campus/29/users";
    private static final String LOCATIONS_PATH = "v2/campus/29/locations";
    private static final String HANE_PATH = "https://api.24hoursarenotenough.42seoul.kr/ext/where42/where42/";
    private static final String SORT = "sort";
    private static final String FILTER = "filter[kind]";
    private static final String PAGE_SIZE = "page[size]";
    private static final String PAGE_NUMBER = "page[number]";
    private static final String RANGE_BEGIN = "range[begin_at]";
    private static final String RANGE_END = "range[end_at]";
    private static final String RANGE_LOGIN = "range[login]";
    private static final String DELIMITER = ",";
    private static final int LOGIN_COUNT = 100;
    private static final int SEARCH_COUNT = 10;

    /**
     * oauth Token 요청 URI 반환
     */
    public static URI token() {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(TOKEN_PATH)
                .build()
                .toUri();
    }

    /**
     * 나의 정보를 조회하는 URI 반환
     */
    public static URI me() {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(ME_PATH)
                .build()
                .toUri();
    }

    /**
     * 특정 42서울 카뎃 정보 요청하는 URI 반환
     */
    public static URI cadetInfo(final String intraName) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(CADET_PATH + intraName)
                .build()
                .toUri();
    }


    /**
     * 42서울 카뎃의 이미지를 요청하는 URI 반환
     */
    public static URI image(final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(USERS_PATH)
                .queryParam(SORT, "login")
                .queryParam(FILTER, "student")
                .queryParam(PAGE_SIZE, LOGIN_COUNT)
                .queryParam(PAGE_NUMBER, page)
                .build()
                .toUri();
    }

    /**
     * 42서울 클러스터 MAC에 로그인하고 있는 카뎃 별 정보 요청 URI 반환
     */
    public static URI loginCadet(final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(LOCATIONS_PATH)
                .queryParam(PAGE_SIZE, LOGIN_COUNT)
                .queryParam(PAGE_NUMBER, page)
                .queryParam(SORT, "-end_at")
                .build()
                .toUri();
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그인 혹은 로그아웃 한 카뎃 요청 URI 반환
     */
    public static URI loginBeforeFiveMinute(final int page, final boolean login) {
        final Date currentTime = new Date();

        final UriComponentsBuilder builder =
                UriComponentsBuilder
                        .newInstance()
                        .scheme(HTTPS)
                        .host(HOST)
                        .path(LOCATIONS_PATH)
                        .queryParam(PAGE_SIZE, LOGIN_COUNT)
                        .queryParam(PAGE_NUMBER, page);

        if (login) {
            builder.queryParam(RANGE_BEGIN,
                    formatDate(calculateMinute(currentTime)) + DELIMITER + formatDate(currentTime));

            return builder.build().toUri();
        }
        builder.queryParam(RANGE_END,
                formatDate(calculateMinute(currentTime)) + DELIMITER + formatDate(currentTime));

        return builder.build().toUri();
    }

    private static Date calculateMinute(final Date date) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);

        return calendar.getTime();
    }

    private static String formatDate(final Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(date);
    }

    /**
     * 조회를 시작하려는 첫 단어와 마지막 단어를 param으로 받아, 검색하는 URI 반환
     */
    public static URI searchCadets(final String begin, final String end, final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS)
                .host(HOST)
                .path(USERS_PATH)
                .queryParam(SORT, "login")
                .queryParam(RANGE_LOGIN, begin + DELIMITER + end)
                .queryParam(PAGE_SIZE, SEARCH_COUNT)
                .queryParam(PAGE_NUMBER, page)
                .build()
                .toUri();
    }

    /**
     * hane 요청 URI 생성
     */
    public static URI hane(final String name) {
        return UriComponentsBuilder
                .fromHttpUrl(HANE_PATH + name)
                .build()
                .toUri();
    }
}
