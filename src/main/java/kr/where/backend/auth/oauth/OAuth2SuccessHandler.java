package kr.where.backend.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.OauthTokenService;
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
    private final OauthTokenService oauthTokenService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        final OAuth2Attribute oAuth2User = (OAuth2Attribute) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        final Member member = memberService.findOne(oAuth2User.getId())
                .orElseGet(null);

        if (member == null) {
            getRedirectStrategy()
                    .sendRedirect(
                            request,
                            response,
                            UriComponentsBuilder
                                    .fromUriString("/v3/join")
                                    .queryParam("id", oAuth2User.getId())
                                    .toUriString()
                            // 프런트 분들에게 경로를 상의한후 만들기
                    );
        }

        log.info("JWT 토큰 발행 시작");

        final String accessToken = jwtService.createAccessToken(oAuth2User.getId());

        // refreshToken 발급 & DB 저장
//        final String refreshToken = jwtService.createRefreshToken(oAuth2User.getId());

        //jwt refreshToken 저장
        jwtService.updateJsonWebToken(oAuth2User.getId());

        getRedirectStrategy().sendRedirect(
                request,
                response,
                UriComponentsBuilder
                        .fromUriString("/v3/token")
                        .queryParam("accessToken", accessToken)
                        .build()
                        .toUriString()
        );
    }
}
