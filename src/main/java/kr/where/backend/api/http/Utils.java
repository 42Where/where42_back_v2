package kr.where.backend.api.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private static String clientId;
    private static String secret;
    private static String redirectUri;

    // 환경변수를 static 함수에서 static 변수로 사용하기 위한 방법
    @Value("${back.client-id}")
    private void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    @Value("${back.secret}")
    private void setSecret(final String secret) {
        this.secret = secret;
    }

    @Value("${back.redirect-uri}")
    private void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getRedirectUri() {
        return redirectUri;
    }
}
