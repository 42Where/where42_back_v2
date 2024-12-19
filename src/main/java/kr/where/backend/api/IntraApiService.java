package kr.where.backend.api;

import java.util.List;
import kr.where.backend.api.exception.RequestException.TooManyRequestException;
import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.ClusterInfo;
import kr.where.backend.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntraApiService {

    private static final String END_DELIMITER = "z";

    /**
     * 특정 카텟의 정보 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CadetPrivacy getCadetPrivacy(final String token, final String name) {
        return JsonMapper
                .mapping(
                        HttpResponse.getMethod(HttpHeader.request42Info(token), UriBuilder.cadetInfo(name)),
                        CadetPrivacy.class);
    }

    /**
     * index page 별로 image 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<CadetPrivacy> getCadetsImage(final String token, final int page) {
        return JsonMapper
                .mappings(
                        HttpResponse.getMethod(HttpHeader.request42Info(token), UriBuilder.image(page)),
                        CadetPrivacy[].class);
    }

    /**
     * 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<ClusterInfo> getCadetsInCluster(final String token, final int page) {
        return JsonMapper
                .mappings(
                        HttpResponse.getMethod(HttpHeader.request42Info(token), UriBuilder.loginCadet(page)),
                        ClusterInfo[].class);
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그아웃 한 카뎃 index page 별로 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<ClusterInfo> getLogoutCadetsLocation(final String token, final int page) {
        return JsonMapper
                .mappings(HttpResponse.getMethod(HttpHeader.request42Info(token),
                                UriBuilder.loginBeforeFiveMinute(page, false)), ClusterInfo[].class);
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<ClusterInfo> getLoginCadetsLocation(final String token, final int page) {
        return JsonMapper
                .mappings(HttpResponse.getMethod(HttpHeader.request42Info(token),
                        UriBuilder.loginBeforeFiveMinute(page, true)), ClusterInfo[].class);
    }

    /**
     * keyWord 부터 end 까지 intra id를 가진 카뎃 10명의 정보 반환
     */
    @Retryable(retryFor = TooManyRequestException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<CadetPrivacy> getCadetsInRange(final String token, final String keyWord, final int page) {
        return JsonMapper
                .mappings(HttpResponse.getMethod(
                                HttpHeader.request42Info(token),
                                UriBuilder.searchCadets(keyWord, keyWord + END_DELIMITER, page)),
                        CadetPrivacy[].class);
    }

    /**
     * 요청 3번 실패 시 실행되는 메서드
     */
    @Recover
    public CadetPrivacy fallbackCadetPrivacy(final CustomException exception) {
        log.warn("[IntraApiService] CadetPrivacy method");
        throw exception;
    }

    @Recover
    public List<CadetPrivacy> fallbackCadetsPrivacy(final CustomException exception) {
        log.warn("[IntraApiService] List<CadetPrivacy> method");
        throw exception;
    }

    @Recover
    public List<ClusterInfo> fallbackClusterList(final CustomException exception) {
        log.warn("[IntraApiService] List<Cluster> method");
        throw exception;
    }
}
