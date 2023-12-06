package kr.where.backend.api;

import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.json.Hane;
import kr.where.backend.api.exception.RequestException.HaneRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaneApiService {
    /**
     * hane api 호출하여 in, out state 반환
     */
    public Hane getHaneInfo(final String name, final String token) {
        try {
            return JsonMapper
                    .mapping(HttpResponse.getMethod(HttpHeader.requestHaneInfo(token), UriBuilder.hane(name)),
                            Hane.class);
        } catch (HaneRequestException e) {
            log.info(e.toString());
            return new Hane();
        }
    }
}
