package kr.where.backend.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.jwt.JsonWebTokenService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.token.TokenService;
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
//    private final TokenService tokenService;
//    private final UserRequestMapper userRequestMapper;
    private static final String TOKEN = "hane";
    private final MemberService memberService;
    private final HaneApiService haneApiService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final JsonWebTokenService jsonWebTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        final OAuth2Attribute oAuth2User = (OAuth2Attribute) authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        memberService.findOne(oAuth2User.getId())
                .orElseGet(() -> createMember(oAuth2User));

        // 최초 로그인이라면 회원가입 처리를 한다.


        log.info("토큰 발행 시작");

        final String accessToken = jsonWebTokenService.createAccessToken(oAuth2User.getId());

        // refreshToken 발급 & DB 저장
        final String refreshToken = jsonWebTokenService.createRefreshToken(oAuth2User.getId());

        final String targetUrl = UriComponentsBuilder.fromUriString("/accessToken")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
        log.info(targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private Member createMember(final OAuth2Attribute oAuth2Attribute) {
        final CadetPrivacy cadetPrivacy = CadetPrivacy
                .createForTest(
                        oAuth2Attribute.getId(),
                        oAuth2Attribute.getLogin(),
                        oAuth2Attribute.getImage(),
                        oAuth2Attribute.getLocation(),
                        oAuth2Attribute.isActive(),
                        oAuth2Attribute.getCreated_at()
                );

        final Hane hane = haneApiService
                .getHaneInfo(
                        oAuth2Attribute.getLogin(),
                        tokenService.findAccessToken(TOKEN)
                );

        return memberService.createAgreeMember(cadetPrivacy, hane);
    }
}
