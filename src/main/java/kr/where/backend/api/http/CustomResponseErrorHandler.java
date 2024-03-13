package kr.where.backend.api.http;

import java.io.IOException;
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

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new ApiUnauthorizedException();
        } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
            throw new TooManyRequestException();
        } else {
            log.error("error code : {}", response.getStatusCode());
            throw new BadRequestException();
        }
    }
}