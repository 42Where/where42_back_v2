package kr.where.backend.token;

import java.time.Duration;
import java.time.LocalDateTime;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.dto.OAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenApiService tokenApiService;

    @Transactional
    public void createToken(final String name, final OAuthToken oAuthToken) {
        validateName(name);
        final Token token = new Token(name, oAuthToken);
        tokenRepository.save(token);
        log.info("[createToken] {} Token 이 생성되었습니다.", name);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException();
        }
        tokenRepository.findByName(name).ifPresent(present -> {
            throw new RuntimeException();
        });
    }

    @Transactional
    public void deleteToken(final String name) {
        Token token = tokenRepository.findByName(name).orElseThrow(RuntimeException::new);
        tokenRepository.delete(token);
    }

    @Transactional
    public String findAccessToken(String name) {
        Token token = tokenRepository.findByName(name).orElseThrow(RuntimeException::new);
        if (isTimeOver(token.getCreatedAt())) {
            updateToken(token);
        }
        return token.getAccessToken();
    }

    private boolean isTimeOver(LocalDateTime createdAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, currentTime);
        Long minute = Math.abs(duration.toMinutes());
        log.info("[accessToken] : Token 을 발급받은지 {}분 지났습니다.", minute);
        if (minute > 60) {
            return true;
        }
        return false;
    }

    private void updateToken(Token token) {
        OAuthToken oAuthToken = tokenApiService.getOAuthTokenWithRefreshToken("", token.getRefreshToken());
        token.updateToken(oAuthToken);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", token.getName());
    }
}
