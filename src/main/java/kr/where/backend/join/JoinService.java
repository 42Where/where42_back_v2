package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.join.dto.ResponseJoin;
import kr.where.backend.jwt.JsonWebToken;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.oauthtoken.OauthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private static final String TOKEN_ADMIN = "admin";
    private static final String TOKEN_HANE = "hane";
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final OauthTokenService oauthTokenService;
    private final HaneApiService haneApiService;
    private final JwtService jwtService;

    @Transactional
    public void join(final Integer intraId) {

//        final CadetPrivacy cadetPrivacy = intraApiService
//                .getCadetPrivacy(
//                        "6d77198541c1fdca671c2f683726df5481f8f0c54df93153cefb8fdcff8e9e5c",
////                        oauthTokenService.findAccessToken(TOKEN_ADMIN),
//                        login
//                );
//
//        memberService.createAgreeMember(
//                cadetPrivacy,
//                haneApiService
//                        .getHaneInfo(
//                                login,
//                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRmdW5jIjoiV2hlcmU0MiIsImlhdCI6MTY3NTE3NzIwMCwiZXhwIjoxNzA0MDM4NDAxfQ.qHT9EtQt3fSXRx3UKfljuhSXdGXImhvH45_anKGNWsI"))
//                .getId();
//
//        final String accessToken = jwtService.createAccessToken(cadetPrivacy.getId(), cadetPrivacy.getLogin());
//
//        jwtService.create(cadetPrivacy.getId(), jwtService.createRefreshToken(cadetPrivacy.getId(), cadetPrivacy.getLogin()));

//        return ResponseJoin.builder().accessToken(accessToken).build();
        final Member member = memberService.findOne(intraId).orElseThrow(MemberException.NoMemberException::new);

//        memberService.createAgreeMember(CadetPrivacy.createForTest(cadetInfo.getId(), cadetInfo.getLogin(), cadetInfo.getLocation(), cadetInfo.getImage(), cadetInfo.isActive(), cadetInfo.getCreated_at())
//        ))
        member.setAgree(true);
        member.setInCluster(haneApiService
                        .getHaneInfo(
                                member.getIntraName(),
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRmdW5jIjoiV2hlcmU0MiIsImlhdCI6MTY3NTE3NzIwMCwiZXhwIjoxNzA0MDM4NDAxfQ.qHT9EtQt3fSXRx3UKfljuhSXdGXImhvH45_anKGNWsI"));
        final JsonWebToken jsonWebToken = new JsonWebToken(member.getIntraId(), jwtService.createRefreshToken(member.getIntraId(), member.getIntraName()));
        jwtService.save(jsonWebToken);
    }
}
