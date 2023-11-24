package kr.where.backend.token;

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
        if (token.isTimeOver()) {
            updateToken(token);
        }
        return token.getAccessToken();
    }

    public void updateToken(final Token token) {
        final OAuthToken oAuthToken = tokenApiService.getOAuthTokenWithRefreshToken(token.getRefreshToken());
        token.updateToken(oAuthToken);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", token.getName());
    }
}
