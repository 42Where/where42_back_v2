package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.jwt.JsonWebToken;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private static final String TOKEN_HANE = "hane";
    private final MemberService memberService;
    private final OAuthTokenService oAuthTokenService;
    private final HaneApiService haneApiService;
    private final JwtService jwtService;

    @Transactional
    public void join(final Integer intraId) {

        final Member member = memberService.findOne(intraId).orElseThrow(MemberException.NoMemberException::new);
        member.setAgree(true);
        member.setInCluster(haneApiService
                        .getHaneInfo(
                                member.getIntraName(), oAuthTokenService.findAccessToken(TOKEN_HANE)));
        final JsonWebToken jsonWebToken = new JsonWebToken(member.getIntraId(), jwtService.createRefreshToken(member.getIntraId(), member.getIntraName()));
        jwtService.save(jsonWebToken);
    }
}
