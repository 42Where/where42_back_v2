package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.join.dto.ResponseJoin;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private static final String TOKEN_ADMIN = "admin";
    private static final String TOKEN_HANE = "hane";
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final OAuthTokenService oauthTokenService;
    private final HaneApiService haneApiService;
    private final JwtService jwtService;

    public ResponseJoin join(final String login) {

        final CadetPrivacy cadetPrivacy = intraApiService
                .getCadetPrivacy(
                        oauthTokenService.findAccessToken(TOKEN_ADMIN),
                        login
                );

        memberService.createAgreeMember(
                cadetPrivacy,
                haneApiService
                        .getHaneInfo(
                                login, oauthTokenService
                                        .findAccessToken(TOKEN_HANE))).getId();

        final String accessToken = jwtService.createAccessToken(cadetPrivacy.getId());

        jwtService.create(cadetPrivacy.getId(), jwtService.createRefreshToken(cadetPrivacy.getId()));

        return ResponseJoin.builder().accessToken(accessToken).build();
    }
}
