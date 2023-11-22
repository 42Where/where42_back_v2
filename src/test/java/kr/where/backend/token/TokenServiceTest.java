package kr.where.backend.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kr.where.backend.api.dto.OAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void 토큰_생성_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();

        // when
        tokenService.createToken(name, oAuthToken);

        // then
        assertEquals(tokenRepository.findAll().size(), 1);
        assertEquals(tokenRepository.findAll().get(0).getName(), name);
    }

    @Test
    public void 토큰_이름_중복_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();

        // when
        tokenService.createToken(name, oAuthToken);

        // then
        assertThrows(Exception.class, () -> tokenService.createToken(name, oAuthToken));
    }

    @Test
    public void 토큰_삭제_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();
        tokenService.createToken(name, oAuthToken);

        // when
        tokenService.deleteToken(name);

        // then
        assertEquals(tokenRepository.findAll().size(), 0);
    }

    @Test
    public void 토큰_찾기_테스트() {
        // given
        String name = "back";
        OAuthToken oAuthToken = new OAuthToken();
        tokenService.createToken(name, oAuthToken);

        // when
        String accessToken = tokenService.findAccessToken(name);

        // then
        assertEquals(oAuthToken.getAccess_token(), accessToken);
    }
}
