package kr.where.backend.oauthtoken;

import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.json.OAuthToken;
import kr.where.backend.oauthtoken.exception.OauthTokenException.DuplicatedTokenNameException;
import kr.where.backend.oauthtoken.exception.OauthTokenException.InvalidOAuthTokenException;
import kr.where.backend.oauthtoken.exception.OauthTokenException.InvalidTokenNameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthTokenService {

    private final OauthTokenRepository oauthTokenRepository;
    private final TokenApiService tokenApiService;

    @Transactional
    public void createToken(final String name, final OAuthToken oAuthToken) {
        validateName(name);
        final OauthToken oauthToken = new OauthToken(name, oAuthToken);
        oauthTokenRepository.save(oauthToken);
        log.info("[createToken] {} Token 이 생성되었습니다.", name);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidTokenNameException();
        }
        oauthTokenRepository.findByName(name).ifPresent(present -> {
            throw new DuplicatedTokenNameException();
        });
    }

    @Transactional
    public void deleteToken(final String name) {
        final OauthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidOAuthTokenException::new);
        oauthTokenRepository.delete(oauthToken);
    }

    @Transactional
    public String findAccessToken(final String name) {
        final OauthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidOAuthTokenException::new);
        if (oauthToken.isTimeOver()) {
            updateToken(oauthToken);
        }
        return oauthToken.getAccessToken();
    }

    public void updateToken(final OauthToken oauthToken) {
        final OAuthToken oAuthToken = tokenApiService.getOAuthTokenWithRefreshToken(oauthToken.getRefreshToken());
        oauthToken.updateToken(oAuthToken);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", oauthToken.getName());
    }

    public void updateHaneToken(final String accessToken) {
        final OauthToken oauthToken = oauthTokenRepository.findByName("hane").orElseThrow(InvalidOAuthTokenException::new);
        oauthToken.updateToken(accessToken);
        log.info("[updateToken] {} Token 이 업데이트 되었습니다.", "hane");
    }
}
