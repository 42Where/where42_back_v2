package kr.where.backend.api.http;

import java.io.IOException;
import kr.where.backend.api.exception.RequestException.ApiUnauthorizedException;
import kr.where.backend.api.exception.RequestException.BadRequestException;
import kr.where.backend.api.exception.RequestException.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            new ApiUnauthorizedException();
        } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
            new TooManyRequestException();
        } else {
            new BadRequestException();
        }
    }
}