package kr.where.backend.api;

import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.exception.request.RequestException.HaneRequestException;
import kr.where.backend.api.mappingDto.Planet;
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
                    .mapping(HttpResponse.postMethod(HttpHeader.request42Info(token), UriBuilder.hane(name)),
                            Hane.class);
            if (hane.getInoutState().equalsIgnoreCase("OUT")) {
                return null;
            }
            if (hane.getCluster().equalsIgnoreCase("GAEPO")) {
                return Planet.gaepo;
            }
            return null;
        } catch (HaneRequestException e) {
            log.info(e.toString());
            return Planet.error;
        }
    }
}
