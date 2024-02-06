package kr.where.backend.oauthtoken;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kr.where.backend.api.json.OAuthTokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OauthOAuthTokenServiceTest {

    @Autowired
    private OAuthTokenService oauthTokenService;
    @Autowired
    private OAuthTokenRepository oauthTokenRepository;

    @Test
    public void 토큰_생성_테스트() {
        // given
        String name = "back";
        OAuthTokenDto oAuthTokenDto = new OAuthTokenDto();

        // when
        oauthTokenService.createToken(name, oAuthTokenDto);

        // then
        assertDoesNotThrow(() -> oauthTokenRepository.findByName(name).orElseThrow());
        assertEquals(oauthTokenRepository.findByName(name).get().getName(), name);
    }
}
