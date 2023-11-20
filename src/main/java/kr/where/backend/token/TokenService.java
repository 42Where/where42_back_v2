package kr.where.backend.token;

import kr.where.backend.api.dto.OAuthToken;
import org.springframework.transaction.annotation.Transactional;

public class TokenService {
    @Transactional
    public void createToken(String name, OAuthToken oAuthToken) {
        final Token token = new Token(name, oAuthToken);
    }
}
