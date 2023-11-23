package kr.where.backend.token;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TimeZone;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.mappingDto.OAuthToken;
import kr.where.backend.exception.token.TokenException.InvalidedTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private static final int TOKEN_EXPIRATION_MINUTES = 60;

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
            throw new RuntimeException("유효하지 않은 이름입니다.");
        }
        tokenRepository.findByName(name).ifPresent(present -> {
            throw new RuntimeException("이미 등록된 토큰입니다.");
        });
    }

    @Transactional
    public void deleteToken(final String name) {
        final Token token = tokenRepository.findByName(name).orElseThrow(InvalidedTokenException::new);
        tokenRepository.delete(token);
    }

    @Transactional
    public String findAccessToken(final String name) {
        final Token token = tokenRepository.findByName(name).orElseThrow(InvalidedTokenException::new);
        if (isTimeOver(name, token.getCreatedAt())) {
            updateToken(token);
        }
        return token.getAccessToken();
    }

    private boolean isTimeOver(final String name, final LocalDateTime createdAt) {
        final LocalDateTime currentTime = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        final Duration duration = Duration.between(currentTime, createdAt);
        final Long minute = Math.abs(duration.toMinutes());
        log.info("[accessToken] {} Token 이 발급된지 {}분 지났습니다.", name, minute);

        if (minute > TOKEN_EXPIRATION_MINUTES) {
            return true;
        }
        return false;
    }

    public void updateToken(final Token token) {
        final OAuthToken oAuthToken = tokenApiService.getOAuthTokenWithRefreshToken(token.getRefreshToken());
        token.updateToken(oAuthToken);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", token.getName());
    }
}
