package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.jwt.dto.ResponseRefreshTokenDTO;
import kr.where.backend.join.exception.JoinException;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.redisToken.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private static final String TOKEN_HANE = "hane";
    private final MemberService memberService;
    private final HaneApiService haneApiService;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;

    @Transactional
    public ResponseRefreshTokenDTO join(final AuthUser authUser) {
        final Member member = memberService.findOne(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        if (member.isAgree()) {
            throw new JoinException.DuplicatedJoinMember();
        }
        memberService.createAgreeMember(
                CadetPrivacy
                        .builder()
                        .build(),
                haneApiService
                        .getHaneInfo(member.getIntraName(), TOKEN_HANE)
        );

        String refreshToken = jwtService.createRefreshToken(authUser.getIntraId(), authUser.getIntraName());
        redisTokenService.saveRefreshToken(member.getIntraId().toString(), refreshToken, 1000L * 60 * 60 * 24 * 7);

        return ResponseRefreshTokenDTO
                .builder()
                .refreshToken(refreshToken)
                .build();
    }
}
