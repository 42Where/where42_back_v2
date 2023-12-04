package kr.where.backend.api;

import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.exception.request.RequestException.HaneRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaneApiService {
    /**
     * hane api 호출하여 inoutState, null 반환
     */
    public Hane getHaneInfo(final String name, final String token) {
        try {
            return JsonMapper
                    .mapping(HttpResponse.postMethod(HttpHeader.request42Info(token), UriBuilder.hane(name)),
                            Hane.class);
        } catch (HaneRequestException e) {
            log.info(e.toString());
            return null;
        }
    }
}
