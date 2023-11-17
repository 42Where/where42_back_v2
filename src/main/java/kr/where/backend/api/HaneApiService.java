package kr.where.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
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
     * hane api 호출하여 gaepo, null, error 반환 -> in, out, error 반환 (혹은 error 도 out 으로 표시될 테니 in, out 반환?)
     */
    public Planet getHaneInfo(String name, String token) {
        request = requestHaneApiHeader(token);
        try {
            response = getResponseApi(request, requestHaneApiUri(name));
            Hane hane = haneMapping(response.getBody());
            if (hane.getInoutState().equalsIgnoreCase("OUT")) {
                return null;
            }
            if (hane.getCluster().equalsIgnoreCase("GAEPO")) {
                return Planet.gaepo;
            }
            return null;
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
