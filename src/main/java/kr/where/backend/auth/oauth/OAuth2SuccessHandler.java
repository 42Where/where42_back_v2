package kr.where.backend.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.OauthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String TOKEN = "hane";
    private final MemberService memberService;
    private final HaneApiService haneApiService;
    private final OauthTokenService oauthTokenService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        final OAuth2User userProfile = (OAuth2User) authentication.getPrincipal();
        final Integer intraId = (Integer) userProfile.getAttributes().get("id");
        final String intraName = (String) userProfile.getAttributes().get("login");

        log.info("Principal에서 꺼낸 OAuth2User = {}", userProfile);

        final Member member = memberService.findOne(intraId)
                .orElse(null);

        if (member == null) {
            getRedirectStrategy()
                    .sendRedirect(
                            request,
                            response,
                            UriComponentsBuilder
                                    .fromUriString("/join")
                                    .queryParam("get_login", intraName)
                                    .toUriString()
                            // 프런트 분들에게 경로를 상의한후 만들기
                    );
            return ;
        }

        log.info("JWT 토큰 발행 시작");

        final String accessToken = jwtService.createAccessToken(intraId);

        // refreshToken 발급 & DB 저장
//        final String refreshToken = jwtService.createRefreshToken(oAuth2User.getId());

        //jwt refreshToken 저장
        jwtService.updateJsonWebToken(intraId);

        getRedirectStrategy()
                .sendRedirect(
                        request,
                        response,
                        UriComponentsBuilder
                                .fromUriString("/token")
                                .queryParam("accessToken", accessToken)
                                .build()
                                .toUriString()
                        // 프런트 분들에게 경로를 상의한후 만들기
        );
    }
}
