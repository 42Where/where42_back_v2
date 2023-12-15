package kr.where.backend.oauthtoken;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kr.where.backend.api.json.OAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OauthOauthTokenServiceTest {

    @Autowired
    private OauthTokenService oauthTokenService;
    @Autowired
    private OauthTokenRepository oauthTokenRepository;

    @Test
    public void 토큰_생성_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();

        // when
        oauthTokenService.createToken(name, oAuthToken);

        // then
        assertDoesNotThrow(() -> oauthTokenRepository.findByName(name).orElseThrow());
        assertEquals(oauthTokenRepository.findByName(name).get().getName(), name);
    }

    @Test
    public void 토큰_이름_중복_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();

        // when
        oauthTokenService.createToken(name, oAuthToken);

        // then
        assertThrows(Exception.class, () -> oauthTokenService.createToken(name, oAuthToken));
    }

    @Test
    public void 토큰_삭제_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();
        oauthTokenService.createToken(name, oAuthToken);

        // when
        oauthTokenService.deleteToken(name);

        // then
        assertThrows(Exception.class, () -> oauthTokenRepository.findByName(name).orElseThrow());
    }
}
