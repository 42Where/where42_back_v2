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
import kr.where.backend.api.dto.Hane;
import kr.where.backend.api.dto.OAuthToken;
import kr.where.backend.member.DTO.Seoul42;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ApiService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    public final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    HttpHeaders headers;
    HttpEntity<MultiValueMap<String, String>> request;
    MultiValueMap<String, String> params;
    ResponseEntity<String> response;


    /**
     * intra 에 oAuth token 발급 요청 후 토큰을 반환
     * */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public OAuthToken getOAuthToken(String secret, String code) {
        request = request42TokenHeader(secret, code);
        response = postResponseApi(request, req42TokenUri());
        return oAuthTokenMapping(response.getBody());
    }

    public HttpEntity<MultiValueMap<String, String>> request42TokenHeader(String secret, String code) {
        headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","");
        params.add("client_secret", secret);
        params.add("code", code);
        params.add("redirect_uri","http://localhost:8080/v2/auth/callback");
        return new HttpEntity<>(params, headers);
    }

    public ResponseEntity<String> postResponseApi(HttpEntity<MultiValueMap<String, String>> request, URI url) {
        return restTemplate.exchange(
                url.toString(),
                HttpMethod.POST,
                request,
                String.class);
    }

    public URI req42TokenUri() {
        return UriComponentsBuilder.fromHttpUrl("https://api.intra.42.fr/oauth/token")
                .build()
                .toUri();
    }

    /**
     * refreshToken 으로 intra 에 oAuth token 발급 요청 후 토큰 반환
     * */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public OAuthToken getOAuthTokenWithRefreshToken(String secret, String refreshToken) {
        request = request42RefreshHeader(secret, refreshToken);
        response = postResponseApi(request, req42TokenUri());
        return oAuthTokenMapping(response.getBody());
    }

    public HttpEntity<MultiValueMap<String, String>> request42RefreshHeader(String secret, String refreshToken) {
        headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", "");
        params.add("client_secret", secret);
        params.add("refresh_token", refreshToken);
        return new HttpEntity<>(params, headers);
    }

    /**
     * index page 별로 image 반환
     * */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Seoul42> get42Image(String token, int index) {
        request = req42ApiHeader(token);
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

    public ResponseEntity<String> getResponseApi(HttpEntity<MultiValueMap<String, String>> request, URI url) {
        return restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                request,
                String.class);
    }

    // TODO
    /**
     *
     * */
    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42ClusterInfo(String token, int i) {
        request = req42ApiHeader(token);
        response = getResponseApi(request, request42ApiLocationUri(i));
        return clusterMapping(response.getBody());
    }

    public URI request42ApiLocationUri(int i) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path("v2/campus/29/locations")
                .queryParam("page[size]", 100)
                .queryParam("page[number]", i)
                .queryParam("sort", "-end_at")
                .build()
                .toUri();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42LocationEnd(String token, int i) {
        request = req42ApiHeader(token);
        response = getResponseApi(request, req42ApiLocationEndUri(i));
        return clusterMapping(response.getBody());
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Cluster> get42LocationBegin(String token, int i) {
        request = req42ApiHeader(token);
        response = getResponseApi(request, req42ApiLocationBeginUri(i));
        return clusterMapping(response.getBody());
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public Seoul42 getMeInfo(String token) {
        request = req42ApiHeader(aes.decoding(token));
        response = getResponseApi(request, req42MeUri());
        return seoul42Mapping(response.getBody());
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public Seoul42 getUserInfo(String token, String name) {
        request = req42ApiHeader(aes.decoding(token));
        response = getResponseApi(request, req42UserUri(name));
        return seoul42Mapping(response.getBody());
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(1000))
    public List<Seoul42> get42UsersInfoInRange(String token, String begin, String end) {
        request = req42ApiHeader(aes.decoding(token));
        response = getResponseApi(request, req42ApiUsersInRangeUri(begin, end));
        return seoul42ListMapping(response.getBody());
    }

    @Recover
    public Seoul42 fallback(RuntimeException e, String token) {
        log.info("[ApiService] {}", e.getMessage());
        throw new TooManyRequestException();
    }

    public Planet getHaneInfo(String name, String token) {
        request = reqHaneApiHeader(token);
        try {
            response = getResponseApi(request, reqHaneApiUri(name));
            Hane hane = haneMapping(response.getBody());
            if (hane.getInoutState().equalsIgnoreCase("OUT")) {
                Date now = new Date();
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {
                    Date tagAt = dateFormat.parse(hane.getTag_at());
                    if ((now.getTime() - tagAt.getTime()) / 60000 < 60)
                        return Planet.rest;
                    return null;
                } catch (Exception e) {
                    log.info("[SimpleDateFormat Error] \"{}\"님의 태그 시간 계산 오류가 발생하였습니다. 카드 태깅시간과 관계 없이 퇴근으로 표기됩니다.", name);
                    return null;
                }
            }
            if (hane.getCluster().equalsIgnoreCase("GAEPO"))
                return Planet.gaepo;
            return Planet.seocho;
        } catch (RuntimeException e) {
            log.info("[Hane Error] \"{}\"님의 hane api 오류가 발생하였습니다. 발생 에러: {}", name, e.getMessage());
            return Planet.error;
        }
    }





    public HttpEntity<MultiValueMap<String, String>> req42ApiHeader(String token) {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/json;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        return new HttpEntity<>(params, headers);
    }

    public HttpEntity<MultiValueMap<String, String>> reqHaneApiHeader(String token) {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/json;charset=utf-8");
        params = new LinkedMultiValueMap<>();
        return new HttpEntity<>(params, headers);
    }








    public URI req42ApiLocationEndUri(int i) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path(Define.INTRA_VERSION_PATH + "/campus/" + Define.SEOUL + "/locations")
                .queryParam("page[size]", 100)
                .queryParam("page[number]", i)
                .queryParam("range[end_at]", dateFormat.format(calendar.getTime()) + "," + dateFormat.format(date))
                .build()
                .toUri();
    }

    public URI req42ApiLocationBeginUri(int i) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -5);
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path(Define.INTRA_VERSION_PATH + "/campus/" + Define.SEOUL + "/locations")
                .queryParam("page[size]", 50)
                .queryParam("page[number]", i)
                .queryParam("range[begin_at]", dateFormat.format(calendar.getTime()) + "," + dateFormat.format(date))
                .build()
                .toUri();
    }

    public URI req42MeUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path(Define.INTRA_VERSION_PATH + "/me")
                .build()
                .toUri();
    }


    public URI req42UserUri(String name) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path(Define.INTRA_VERSION_PATH + "/users/" + name)
                .build()
                .toUri();
    }

    public URI req42ApiUsersInRangeUri(String begin, String end) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host("api.intra.42.fr").path(Define.INTRA_VERSION_PATH + "/campus/" + Define.SEOUL + "/users")
                .queryParam("sort", "login")
                .queryParam("range[login]", begin + "," + end)
                .queryParam("page[size]", "10")
                .build()
                .toUri();
    }


    public URI reqHaneApiUri(String name) {
        return UriComponentsBuilder.fromHttpUrl("https://api.24hoursarenotenough.42seoul.kr/ext/where42/where42/" + name)
                .build()
                .toUri();
    }


    public OAuthToken oAuthTokenMapping(String body) {
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(body, OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(); // checked exception 좀 더 구체적인 exception 을 던져야 함
        }
        return oAuthToken;
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


    public List<Cluster> clusterMapping(String body) {
        List<Cluster> clusters = null;
        try {
            clusters = Arrays.asList(objectMapper.readValue(body, Cluster[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return clusters;
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

    public Hane haneMapping(String body) {
        Hane hane = null;
        try {
            hane = objectMapper.readValue(body, Hane.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return hane;
    }
}
