package kr.where.backend.api;

import java.util.List;
import kr.where.backend.api.http.HttpRequest;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.Cluster;
import kr.where.backend.member.DTO.Seoul42;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntraApiService {
    /**
     * 요청 3번 실패 시 실행되는 메서드
     */
    @Recover
    public Seoul42 fallback(final RuntimeException e, final String token) {
        log.info("[ApiService] {}", e.getMessage());
        throw new RuntimeException();
    }

    /**
     * 본인 정보 반환
     */
    @Retryable
    public Seoul42 getMeInfo(final String token) {
        return JsonMapper
                .mapping(HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token),
                        UriBuilder.me()), Seoul42.class);
    }

    /**
     * 특정 카텟의 정보 반환
     */
    @Retryable
    public Seoul42 getUserInfo(final String token, final String name) {
        return JsonMapper
                .mapping(
                        HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token), UriBuilder.cadetInfo(name)),
                        Seoul42.class);
    }

    /**
     * index page 별로 image 반환
     */
    @Retryable
    public List<Seoul42> get42Image(final String token, final int page) {
        return JsonMapper
                .mappings(
                        HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token), UriBuilder.image(page)),
                        Seoul42[].class);
    }

    /**
     * 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable
    public List<Cluster> get42ClusterInfo(final String token, final int page) {
        return JsonMapper
                .mappings(
                        HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token), UriBuilder.loginCadet(page)),
                        Cluster[].class);
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그아웃 한 카뎃 index page 별로 반환
     */
    @Retryable
    public List<Cluster> get42LocationEnd(final String token, final int page) {
        return JsonMapper
                .mappings(HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token),
                                UriBuilder.loginBeforeFiveMinute(page, false)), Cluster[].class);
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable
    public List<Cluster> get42LocationBegin(final String token, final int page) {
        return JsonMapper
                .mappings(HttpResponse.responseBodyOfGet(HttpRequest.requestInfo(token),
                        UriBuilder.loginBeforeFiveMinute(page, true)), Cluster[].class);
    }

    /**
     * begin 부터 end 까지 intra id를 가진 카뎃 10명의 정보 반환
     */
    @Retryable
    public List<Seoul42> get42UsersInfoInRange(final String token, final String begin, final String end) {
        return JsonMapper
                .mappings(HttpResponse.responseBodyOfGet(
                                HttpRequest.requestInfo(token), UriBuilder.searchCadets(begin, end)),
                        Seoul42[].class);
    }
}
