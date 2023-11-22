package kr.where.backend.api;

import kr.where.backend.api.http.HttpRequestAndResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.member.Enum.Planet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaneApiService {
    /**
     * hane api 호출하여 gaepo, null, error 반환 -> in, out, error 반환 (혹은 error 도 out 으로 표시될 테니 in, out 반환?)
     */
    public Planet getHaneInfo(final String name, final String token) {
        try {
            final Hane hane = JsonMapper
                    .mapping(HttpRequestAndResponse.responseBodyOfPost(HttpRequestAndResponse.requestInfo(token), UriBuilder.hane(name)),
                            Hane.class);
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
}
