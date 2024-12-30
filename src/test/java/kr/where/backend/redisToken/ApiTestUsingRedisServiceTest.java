package kr.where.backend.redisToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.jwt.dto.RequestReissueDTO;
import kr.where.backend.logout.LogoutService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;



@SpringBootTest
@Transactional
@Rollback
@AutoConfigureMockMvc
public class ApiTestUsingRedisServiceTest {
    @Autowired
    MockMvc mockMvc;

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
        Collection<? extends GrantedAuthority> authorities
                = Stream.of("ROLE_USER")
                .map(SimpleGrantedAuthority::new)
                .toList();
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @Test
    @DisplayName("Mock server를 통해 reissue api 실행 시, jwt claim의 intraId에 해당되는 member가 없을 시 403 에러 test")
    void reissueApiTestWithMockServer403Test() throws Exception {
        //given
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");

        redisTokenService.saveRefreshToken("135436", refreshToken);

        String json = "{\"intraId\": 135436}";

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v3/jwt/reissue")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string("CustomException(errorCode=1000, errorMessage=존재하지 않는 맴버입니다.)")
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Mock server를 통해, 정상적인 reissue test")
    void reissueApiTestWithMockServer200Status() throws Exception {
        //given
        //member 생성
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member m = memberService.createAgreeMember(cadetPrivacy, hane);

        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");

        redisTokenService.saveRefreshToken("135436", refreshToken);

        String json = "{\"intraId\": 135436}";

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v3/jwt/reissue")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("refresh Token이 blackList에 포함시, 401 clinet Error Test")
    void reissueApiTestWhenUsedInvalidedRefreshToken() throws Exception {
        //when

        //member create
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member m = memberService.createAgreeMember(cadetPrivacy, hane);

        //created refreshToken
        String accessToken = jwtService.createAccessToken(135436, "suhwpark");
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");
        redisTokenService.saveRefreshToken("135436", refreshToken);

        //invalidate refreshToken
        redisTokenService.invalidateToken(accessToken, "135436");

        String json = "{\"intraId\": 135436}";

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v3/jwt/reissue")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string("CustomException(errorCode=1504, errorMessage=잘못된 Jwt 토큰입니다.)")
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("invalidated accessToken을 통해, api 요청 시 denied test")
    void apiTestWithInvalidAccessToken() throws Exception {
        //when

        //member create
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member m = memberService.createAgreeMember(cadetPrivacy, hane);

        //created refreshToken
        String accessToken = jwtService.createAccessToken(135436, "suhwpark");
        String refreshToken = jwtService.createRefreshToken(135436, "suhwpark");
        redisTokenService.saveRefreshToken("135436", refreshToken);

        //invalidate refreshToken
        redisTokenService.invalidateToken(accessToken, "135436");

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v3/member")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.
                        content()
                        .string("\"CustomException(errorCode=1500, errorMessage=유효한 Jwt 토큰이 없습니다.)\"")
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("유효하지 않은 intraId를 통한 reissue test 시, 401 error")
    void reissueApiTestWithInvalidIntraId() throws Exception{
        //when
        String json = "{\"intraId\": 123456}";

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v3/jwt/reissue")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers
                        .content()
                        .string("CustomException(errorCode=1000, errorMessage=존재하지 않는 맴버입니다.)")
                )
                .andDo(MockMvcResultHandlers.print());
    }
}

