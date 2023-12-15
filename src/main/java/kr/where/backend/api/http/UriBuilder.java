package kr.where.backend.api.http;

import static kr.where.backend.api.http.Uri.CADET_PATH;
import static kr.where.backend.api.http.Uri.DELIMITER;
import static kr.where.backend.api.http.Uri.FILTER;
import static kr.where.backend.api.http.Uri.HANE_PATH;
import static kr.where.backend.api.http.Uri.HOST;
import static kr.where.backend.api.http.Uri.HTTPS;
import static kr.where.backend.api.http.Uri.LOCATIONS_PATH;
import static kr.where.backend.api.http.Uri.ME_PATH;
import static kr.where.backend.api.http.Uri.PAGE_NUMBER;
import static kr.where.backend.api.http.Uri.PAGE_SIZE;
import static kr.where.backend.api.http.Uri.RANGE_BEGIN;
import static kr.where.backend.api.http.Uri.RANGE_END;
import static kr.where.backend.api.http.Uri.RANGE_LOGIN;
import static kr.where.backend.api.http.Uri.SORT;
import static kr.where.backend.api.http.Uri.TOKEN_PATH;
import static kr.where.backend.api.http.Uri.USERS_PATH;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.web.util.UriComponentsBuilder;

public class UriBuilder {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final int TIME_DIFFERENCE = -5;
    private static final int LOGIN_COUNT = 100;
    private static final int SEARCH_COUNT = 10;

    /**
     * oauth Token 요청 URI 반환
     */
    public static URI token() {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(TOKEN_PATH.getValue())
                .build()
                .toUri();
    }

    /**
     * 나의 정보를 조회하는 URI 반환
     */
    public static URI me() {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(ME_PATH.getValue())
                .build()
                .toUri();
    }

    /**
     * 특정 42서울 카뎃 정보 요청하는 URI 반환
     */
    public static URI cadetInfo(final String intraName) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(CADET_PATH.getValue() + intraName)
                .build()
                .toUri();
    }


    /**
     * 42서울 카뎃의 이미지를 요청하는 URI 반환
     */
    public static URI image(final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(USERS_PATH.getValue())
                .queryParam(SORT.getValue(), "login")
                .queryParam(FILTER.getValue(), "student")
                .queryParam(PAGE_SIZE.getValue(), LOGIN_COUNT)
                .queryParam(PAGE_NUMBER.getValue(), page)
                .build()
                .toUri();
    }

    /**
     * 42서울 클러스터 MAC에 로그인하고 있는 카뎃 별 정보 요청 URI 반환
     */
    public static URI loginCadet(final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(LOCATIONS_PATH.getValue())
                .queryParam(PAGE_SIZE.getValue(), LOGIN_COUNT)
                .queryParam(PAGE_NUMBER.getValue(), page)
                .queryParam(SORT.getValue(), "-end_at")
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
                        .scheme(HTTPS.getValue())
                        .host(HOST.getValue())
                        .path(LOCATIONS_PATH.getValue())
                        .queryParam(PAGE_SIZE.getValue(), LOGIN_COUNT)
                        .queryParam(PAGE_NUMBER.getValue(), page);

        if (login) {
            builder.queryParam(RANGE_BEGIN.getValue(),
                    formatDate(calculateMinute(currentTime)) + DELIMITER.getValue() + formatDate(currentTime));

            return builder.build().toUri();
        }
        builder.queryParam(RANGE_END.getValue(),
                formatDate(calculateMinute(currentTime)) + DELIMITER.getValue() + formatDate(currentTime));

        return builder.build().toUri();
    }

    private static Date calculateMinute(final Date date) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, TIME_DIFFERENCE);

        return calendar.getTime();
    }

    private static String formatDate(final Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(date);
    }

    /**
     * 조회를 시작하려는 첫 단어와 마지막 단어를 param으로 받아, 검색하는 URI 반환
     */
    public static URI searchCadets(final String begin, final String end, final int page) {
        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTPS.getValue())
                .host(HOST.getValue())
                .path(USERS_PATH.getValue())
                .queryParam(SORT.getValue(), "login")
                .queryParam(RANGE_LOGIN.getValue(), begin + DELIMITER.getValue() + end)
                .queryParam(PAGE_SIZE.getValue(), SEARCH_COUNT)
                .queryParam(PAGE_NUMBER.getValue(), page)
                .build()
                .toUri();
    }

    /**
     * hane 요청 URI 생성
     */
    public static URI hane(final String name) {
        return UriComponentsBuilder
                .fromHttpUrl(HANE_PATH.getValue() + name)
                .build()
                .toUri();
    }
}
