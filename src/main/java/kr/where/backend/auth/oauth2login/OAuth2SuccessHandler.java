package kr.where.backend.auth.oauth2login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.CadetPrivacy;
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

        log.info("Principal에서 꺼낸 OAuth2User = {}", userProfile.toString());

        final CadetInfo cadetInfo = CadetInfo.of(userProfile.getAttributes());
        final Member member = memberService.findOne(cadetInfo.getId())
                .orElseGet(() -> memberService.createDisagreeMember(
                        CadetPrivacy.createForTest(cadetInfo.getId(), cadetInfo.getLogin(), cadetInfo.getLocation(), cadetInfo.getImage(), cadetInfo.isActive(), cadetInfo.getCreated_at())
                        )
                );
        /**
         * 무조권 token 발행 -> 없으면 맴버 생성? agree = false로 생성 token 저장
         * jwt name과 id 동의 여부 넣어주기
         *
         * 동의를 받고 -> memeber를 만들지
         * 임의의 member를 만들고 -> 동의를 받았을때, 거부한다면 -> 다시 로그인 페이지로 redicet할지
         *
         */

        //jwt 발행
        log.info("JWT 토큰 발행 시작");

        final String accessToken = jwtService.createAccessToken(cadetInfo.getId(), cadetInfo.getLogin());
        boolean isAgree = false;

        if (member.isAgree()) {
            jwtService.updateJsonWebToken(cadetInfo.getId(), cadetInfo.getLogin());
            isAgree = member.isAgree();
        }

        getRedirectStrategy()
                .sendRedirect(
                        request,
                        response,
                        UriComponentsBuilder
                                .fromUriString("http://13.209.149.15/login-sucess")
                                .queryParam("token", accessToken)
                                .queryParam("intraId",cadetInfo.getId())
                                .queryParam("agreement", isAgree)
                                .build()
                                .toUriString()
                        // 프런트 분들에게 경로를 상의한후 만들기
                );
    }
}
