package kr.where.backend.auth.oauth2login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.auth.filter.JwtConstants;
import kr.where.backend.auth.oauth2login.cookie.CookieShop;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
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
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final int ACCESS_EXPIRY = 30 * 60;
    private static final int REFRESH_EXPIRY = 14 * 24 * 60 * 60;
    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {

        final UserProfile userProfile = (UserProfile) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User Name= {}", userProfile.getName());

        final CadetPrivacy cadetPrivacy = CadetPrivacy.of(userProfile.getAttributes());
        final Member member = memberService.findOne(cadetPrivacy.getId())
                .orElseGet(
                        () -> memberService.createDisagreeMember(cadetPrivacy)
                );

        //jwt 발행
        log.info("JWT 토큰 발행 시작");

        CookieShop.bakedCookie(response,
                JwtConstants.ACCESS.getValue(),
                ACCESS_EXPIRY,
                jwtService.createAccessToken(cadetPrivacy.getId(), cadetPrivacy.getLogin()),
                false
        );

        if (member.isAgree()) {
            CookieShop.bakedCookie(response,
                    JwtConstants.REFRESH.getValue(),
                    REFRESH_EXPIRY,
                    jwtService.createRefreshToken(cadetPrivacy.getId(), cadetPrivacy.getLogin()),
                    true
            );
        }
        getRedirectStrategy()
                .sendRedirect(
                        request,
                        response,
                        UriComponentsBuilder
                                .fromUriString("https://where42.kr")
                                .queryParam("intraId", member.getIntraId())
                                .queryParam("agreement", member.isAgree())
                                .build()
                                .toUriString()
                );
    }
}
