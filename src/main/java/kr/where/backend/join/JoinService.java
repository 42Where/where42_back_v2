package kr.where.backend.join;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.join.dto.ResponseJoinDTO;
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
    public ResponseJoinDTO join(final Integer intraId, final String intraName) {
        final Member member = memberService.findOne(intraId)
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

        final String refreshToken = jwtService.createRefreshToken(intraId, intraName);
        redisTokenService.saveRefreshToken(member.getIntraId().toString(), refreshToken);

        return ResponseJoinDTO
                .builder()
                .intraId(intraId)
                .build();
    }
}
