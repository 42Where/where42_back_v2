package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.jwt.JsonWebToken;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final GroupService groupService;

    @Value("${hane.token.secret}")
    private String haneToken;
    @Transactional
    public void join(final Integer intraId, final AuthUserInfo authUser) {
        final Member member = memberService.findOne(intraId).orElseThrow(MemberException.NoMemberException::new);
        member.setAgree(true);
//        member.setInCluster(haneApiService
//                        .getHaneInfo(
//                                member.getIntraName(), oAuthTokenService.findAccessToken(TOKEN_HANE)));
        member.setInCluster(haneApiService
                        .getHaneInfo(
                                member.getIntraName(), TOKEN_HANE));

        ResponseGroupDTO responseGroupDto = groupService.createGroup(new CreateGroupDTO(Group.DEFAULT_GROUP), authUser);
        member.setDefaultGroupId(responseGroupDto.getGroupId());

        final JsonWebToken jsonWebToken = new JsonWebToken(member.getIntraId(), jwtService.createRefreshToken(member.getIntraId(), member.getIntraName()));
        jwtService.save(jsonWebToken);
    }
}
