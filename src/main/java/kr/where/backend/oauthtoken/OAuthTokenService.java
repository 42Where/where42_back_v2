package kr.where.backend.oauthtoken;

import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.json.OAuthTokenDto;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.DuplicatedTokenNameException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.InvalidTokenNameException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException.InvalidOAuthTokenException;
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

        OAuthToken oauthToken = oauthTokenRepository.findByName(name)
                .map(existingToken -> {
                    existingToken.updateToken(oAuthTokenDto);
                    return existingToken;
                })
                .orElseGet(() -> new OAuthToken(name, oAuthTokenDto));

        oauthTokenRepository.save(oauthToken);
        log.info("[oAuthToken] {} Token 이 생성되었습니다.", name);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidTokenNameException();
        }
    }

    @Transactional
    public void deleteToken(final String name) {
        final OAuthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidOAuthTokenException::new);
        oauthTokenRepository.delete(oauthToken);
    }

    @Transactional
    public String findAccessToken(final String name) {
        final OAuthToken oauthToken = oauthTokenRepository.findByName(name).orElseThrow(InvalidOAuthTokenException::new);
        if (oauthToken.isTimeOver()) {
            updateToken(oauthToken);
        }
        return oauthToken.getAccessToken();
    }

    @Transactional
    public void updateToken(final OAuthToken oauthToken) {
        final OAuthTokenDto oAuthTokenDto = tokenApiService.getOAuthTokenWithRefreshToken(oauthToken.getRefreshToken());
        oauthToken.updateToken(oAuthTokenDto);
        log.info("[oAuthToken] {} Token 이 업데이트 되었습니다.", oauthToken.getName());
    }

    @Transactional
    public void updateHaneToken(final String accessToken) {
        final OAuthToken oauthToken = oauthTokenRepository.findByName("hane")
                .orElseGet(() -> {
                    OAuthToken newToken = new OAuthToken("hane");
                    oauthTokenRepository.save(newToken);
                    return newToken;
                });
        oauthToken.updateToken(accessToken);
        log.info("[oAuthToken] {} Token 이 업데이트 되었습니다.", "hane");
    }
}
