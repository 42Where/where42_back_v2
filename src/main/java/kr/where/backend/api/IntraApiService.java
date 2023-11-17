package kr.where.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import kr.where.backend.api.dto.Cluster;
import kr.where.backend.member.DTO.Seoul42;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntraApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    public final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    HttpHeaders headers;
    HttpEntity<MultiValueMap<String, String>> request;
    MultiValueMap<String, String> params;
    ResponseEntity<String> response;

    public HttpEntity<MultiValueMap<String, String>> request42ApiHeader(String token) {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/json;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        return new HttpEntity<>(params, headers);
    }

    public ResponseEntity<String> getResponseApi(HttpEntity<MultiValueMap<String, String>> request, URI url) {
        return restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                request,
                String.class);
    }

    /**
     * 요청 3번 실패 시 실행되는 메서드
     */
    @Recover
    public Seoul42 fallback(RuntimeException e, String token) {
        log.info("[ApiService] {}", e.getMessage());
        throw new RuntimeException();
    }

    /**
     * 본인 정보 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public Seoul42 getMeInfo(String token) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42MeUri());
        return seoul42Mapping(response.getBody());
    }

    public URI request42MeUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/me")
                .build()
                .toUri();
    }

    public Seoul42 seoul42Mapping(String body) {
        Seoul42 seoul42 = null;
        try {
            seoul42 = objectMapper.readValue(body, Seoul42.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return seoul42;
    }

    /**
     * 특정 카텟의 정보 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public Seoul42 getUserInfo(String token, String name) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42UserUri(name));
        return seoul42Mapping(response.getBody());
    }

    public URI request42UserUri(String name) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/users/" + name)
                .build()
                .toUri();
    }

    /**
     * index page 별로 image 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Seoul42> get42Image(String token, int index) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, reqest42ImageUri(index));
        return seoul42ListMapping(response.getBody());
    }

    public URI reqest42ImageUri(int i) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/campus/29/users")
                .queryParam("sort", "login")
                .queryParam("filter[kind]", "student")
                .queryParam("page[size]", 100)
                .queryParam("page[number]", i)
                .build()
                .toUri();
    }


    /**
     * 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42ClusterInfo(String token, int index) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42ApiLocationUri(index));
        return clusterMapping(response.getBody());
    }

    public URI request42ApiLocationUri(int index) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/campus/29/locations")
                .queryParam("page[size]", 100)
                .queryParam("page[number]", index)
                .queryParam("sort", "-end_at")
                .build()
                .toUri();
    }

    public List<Cluster> clusterMapping(String body) {
        List<Cluster> clusters = null;
        try {
            clusters = Arrays.asList(objectMapper.readValue(body, Cluster[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return clusters;
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그아웃 한 카뎃 index page 별로 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42LocationEnd(String token, int index) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42ApiLocationEndUri(index));
        return clusterMapping(response.getBody());
    }

    public URI request42ApiLocationEndUri(int i) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("vs/campus/29/locations")
                .queryParam("page[size]", 100)
                .queryParam("page[number]", i)
                .queryParam("range[end_at]", dateFormat.format(calendar.getTime()) + "," + dateFormat.format(date))
                .build()
                .toUri();
    }

    /**
     * 5분 이내 클러스터 아이맥에 로그인 한 카뎃 index page 별로 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42LocationBegin(String token, int i) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42ApiLocationBeginUri(i));
        return clusterMapping(response.getBody());
    }

    public URI request42ApiLocationBeginUri(int i) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/campus/29/locations")
                .queryParam("page[size]", 50)
                .queryParam("page[number]", i)
                .queryParam("range[begin_at]", dateFormat.format(calendar.getTime()) + "," + dateFormat.format(date))
                .build()
                .toUri();
    }

    /**
     * begin 부터 end 까지 intra id를 가진 카뎃 10명의 정보 반환
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Seoul42> get42UsersInfoInRange(String token, String begin, String end) {
        request = request42ApiHeader(token);
        response = getResponseApi(request, request42ApiUsersInRangeUri(begin, end));
        return seoul42ListMapping(response.getBody());
    }

    public URI request42ApiUsersInRangeUri(String begin, String end) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/campus/29/users")
                .queryParam("sort", "login")
                .queryParam("range[login]", begin + "," + end)
                .queryParam("page[size]", "10")
                .build()
                .toUri();
    }

    public List<Seoul42> seoul42ListMapping(String body) {
        List<Seoul42> seoul42List = null;
        try {
            seoul42List = Arrays.asList(objectMapper.readValue(body, Seoul42[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return seoul42List;
    }
}
