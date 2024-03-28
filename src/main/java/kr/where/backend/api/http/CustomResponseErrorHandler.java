package kr.where.backend.api.http;

import java.io.IOException;
import kr.where.backend.api.exception.RequestException.ApiServerErrorException;
import kr.where.backend.api.exception.RequestException.ApiUnauthorizedException;
import kr.where.backend.api.exception.RequestException.BadRequestException;
import kr.where.backend.api.exception.RequestException.TooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError();
    }

    /**
     * 외부 api 요청 후 받은 응답의 exception
     *
     * 400 BAD_REQUEST : 잘못된 요청
     * 401 UNAUTHORIZED : 권한 없음 access_token 을 확인
     * 429 TOO_MANY_REQUESTS : 1초에 2번 / 10분에 1200번 요청 횟수 초과 시
     * 500 INTERNAL_SERVER_ERROR : 외부 서버의 에러
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new ApiUnauthorizedException();
        } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
            throw new TooManyRequestException();
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new ApiServerErrorException();
        } else {
            log.error("error code : {}", response.getStatusCode());
            throw new BadRequestException();
        }
    }
}