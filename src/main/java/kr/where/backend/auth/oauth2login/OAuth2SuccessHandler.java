package kr.where.backend.auth.oauth2login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    private final OAuthTokenService oauthTokenService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        final UserProfile userProfile = (UserProfile) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {}", userProfile);

        final CadetInfo cadetInfo = CadetInfo.of(userProfile.getAttributes());
        final Member member = memberService.findOne(cadetInfo.getId())
                .orElse(null);

        if (member == null) {
            getRedirectStrategy()
                    .sendRedirect(
                            request,
                            response,
                            UriComponentsBuilder
                                    .fromUriString("/join")
                                    .queryParam("get_login", cadetInfo.getLogin())
                                    .toUriString()
                            // 프런트 분들에게 경로를 상의한후 만들기
                    );
            return ;
        }

        log.info("JWT 토큰 발행 시작");

        final String accessToken = jwtService.createAccessToken(cadetInfo.getId());

        // refreshToken 발급 & DB 저장
//        final String refreshToken = jwtService.createRefreshToken(oAuth2User.getId());

        //jwt refreshToken 저장
        jwtService.updateJsonWebToken(cadetInfo.getId());

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
