package kr.where.backend.redisToken;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import kr.where.backend.logout.LogoutService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class RedisTokenServiceTest {

    @Autowired
    RedisTokenService redisTokenService;

    @Autowired
    JwtService jwtService;

    @Autowired
    LogoutService logoutService;

    @Autowired
    MemberService memberService;

    AuthUser authUser;
    Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @Test
    @DisplayName("redis에 refreshToken 저장하는 test")
    void saveRefreshToken() {
        //given
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");

        //when
        redisTokenService.saveRefreshToken("135436", refreshToken);

        //then
        String viewedToken = redisTokenService.getRefreshToken("135436");

        assertThat(refreshToken).isEqualTo(viewedToken);
    }

    @Test
    @DisplayName("reissue 시 redis에서 refreshToken을 사용하여 재발급 test")
    void reissueWithRedis() {
        //given

        //member create
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member m = memberService.createAgreeMember(cadetPrivacy, hane);

        String accessToken = jwtService.createAccessToken(135436, "suhwpark");
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");

        redisTokenService.saveRefreshToken("135436", refreshToken);

        //when
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseAccessTokenDTO dto = jwtService.reissueAccessToken(response, 135436);

        //then
        assertThat(Objects.equals(accessToken, dto.getAccessToken())).isFalse();
    }

    @Test
    @DisplayName("logout 시, accessToken과 refreshToken 삭제되고 blackList에 포함되는지 test")
    void invalidateTokensWhenMemberLogout() {
        //given
        String accessToken = jwtService.createAccessToken(135436, "suhwpark");
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");

        redisTokenService.saveRefreshToken("135436", refreshToken);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        //when
        logoutService.logout(request, 135436);

        //then
        assertThat(redisTokenService.isAccessTokenInBlackList(accessToken)).isTrue();
    }

    @Test
    @DisplayName("member 삭제 시, accessToken과 refreshToken 삭제되고 blackList에 포함되는지 test")
    void invalidTokensWhenMemberDeleted() {
        //given

        //member 생성
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

        //token 생성
        String accessToken = jwtService.createAccessToken(135436, "suhwpark");
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");
        redisTokenService.saveRefreshToken("135436", refreshToken);

        //request 설정
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        //when
        memberService.deleteMember(request, authUser);

        //then
        assertThat(redisTokenService.isAccessTokenInBlackList(accessToken)).isTrue();
    }


}
