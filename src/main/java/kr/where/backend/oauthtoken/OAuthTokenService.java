package kr.where.backend.oauthtoken;

import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.json.OAuthTokenDto;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.InvalidedOAuthTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthTokenService {

    private final OAuthTokenRepository oauthTokenRepository;
    private final TokenApiService tokenApiService;

    @Transactional
    public void createToken(final String name, final OAuthTokenDto oAuthTokenDto) {
        validateName(name);
        final OAuthToken oauthToken = new OAuthToken(name, oAuthTokenDto);
        oauthTokenRepository.save(oauthToken);
        log.info("[createToken] {} Token 이 생성되었습니다.", name);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("유효하지 않은 이름입니다.");
        }
        oauthTokenRepository.findByName(name).ifPresent(present -> {
            throw new RuntimeException("이미 등록된 토큰입니다.");
        });
    }

    @Transactional
    public void deleteToken(final String name) {
        final OAuthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidedOAuthTokenException::new);
        oauthTokenRepository.delete(oauthToken);
    }

    @Transactional
    public String findAccessToken(final String name) {
        final OAuthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidedOAuthTokenException::new);
        if (oauthToken.isTimeOver()) {
            updateToken(oauthToken);
        }
        return oauthToken.getAccessToken();
    }

    public void updateToken(final OAuthToken oauthToken) {
        final OAuthTokenDto oAuthTokenDto = tokenApiService.getOAuthTokenWithRefreshToken(oauthToken.getRefreshToken());
        oauthToken.updateToken(oAuthTokenDto);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", oauthToken.getName());
    }
}
