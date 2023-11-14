package kr.where.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import kr.where.backend.api.dto.Hane;
import kr.where.backend.member.Enum.Planet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaneApiService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private HttpHeaders headers;
    private HttpEntity<MultiValueMap<String, String>> request;
    private MultiValueMap<String, String> params;
    private ResponseEntity<String> response;

    public HttpEntity<MultiValueMap<String, String>> requestHaneApiHeader(String token) {
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
     * 서초가 없어질 시 수정
     * <p>
     * hane api 호출하여 정보 파싱 후 반환 hane api가 "GAEPO", "SEOCHO" 반환 시 해당 planet 반환 "OUT" 반환 시 마지막 태그 시간이 60분 이전일 경우 rest, 이후일
     * 경우 out 반환 사용자의 카드 태깅 오류 등으로 오류 발생 시 error를 반환하여 hane를 타지 않고 42api 정보만 조회할 수 있도록 함 simpleDateFormat 오류 발생 시 태깅 시간과
     * 상관 없이 out 반환
     */
    public Planet getHaneInfo(String name, String token) {
        request = requestHaneApiHeader(token);
        try {
            response = getResponseApi(request, requestHaneApiUri(name));
            Hane hane = haneMapping(response.getBody());
            if (hane.getInoutState().equalsIgnoreCase("OUT")) {
                Date now = new Date();
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {
                    Date tagAt = dateFormat.parse(hane.getTag_at());
                    if ((now.getTime() - tagAt.getTime()) / 60000 < 60) {
                        return Planet.rest;
                    }
                    return null;
                } catch (Exception e) {
                    log.info("[SimpleDateFormat Error] \"{}\"님의 태그 시간 계산 오류가 발생하였습니다. 카드 태깅시간과 관계 없이 퇴근으로 표기됩니다.",
                            name);
                    return null;
                }
            }
            if (hane.getCluster().equalsIgnoreCase("GAEPO")) {
                return Planet.gaepo;
            }
            return Planet.seocho;
        } catch (RuntimeException e) {
            log.info("[Hane Error] \"{}\"님의 hane api 오류가 발생하였습니다. 발생 에러: {}", name, e.getMessage());
            return Planet.error;
        }
    }

    public URI requestHaneApiUri(String name) {
        return UriComponentsBuilder.fromHttpUrl(
                        "https://api.24hoursarenotenough.42seoul.kr/ext/where42/where42/" + name)
                .build()
                .toUri();
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
