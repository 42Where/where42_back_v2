package kr.where.backend.api.http;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpResponse {

    public static String getMethod(final HttpEntity<MultiValueMap<String, String>> request,
                                   final URI uri) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        return restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class).getBody();
    }

    public static String postMethod(final HttpEntity<?> request,
                                    final URI uri) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        return restTemplate.exchange(uri.toString(), HttpMethod.POST, request, String.class).getBody();
    }
}
