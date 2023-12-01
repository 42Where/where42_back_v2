package kr.where.backend.update;

import java.util.ArrayList;
import java.util.List;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.mappingDto.Cluster;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
@Transactional(readOnly = true)
public class UpdateService {

    private final TokenService tokenService;
    private final HaneApiService haneApiService;
    private final IntraApiService intraApiService;
    private final MemberService memberService;

    //TODO
    /**
     * 1. 로그인한 모든 카뎃의 위치 업데이트 서비스
     * 2. 모든 3분마다 카뎃의 로그인 여부에 따른 위치 업데이트 서비스
     * 3. 새로운 기수 들어왔을 때, image 업데이트 서비스
     */

    /**
     * where42 서비스가 1시간 이상 다운 되었을때, 42 서울 로그인한 카뎃에 대한 위치 업데이트 42api를 호출하기 때문에 admin 토큰 호출(api 호출 제한)
     */
    @Transactional
    public void updateMemberLocations() {
        final String token = tokenService.findAccessToken("admin");
        final String hane = tokenService.findAccessToken("Hane");

        final List<List<Cluster>> loginMember = getLoginMember(token);

        for (List<Cluster> members : loginMember) {
            updateLocation(members, hane);
        }
    }

    private List<List<Cluster>> getLoginMember(final String token) {
        int page = 1;
        List<List<Cluster>> result = new ArrayList<>();

        while (true) {
            List<Cluster> loginMember = intraApiService.getCadetsInCluster(token, page);
            result.add(loginMember);
            if (loginMember.size() != 100) {
                break;
            }
            page++;
        }

        return result;
    }


    private void updateLocation(final List<Cluster> cadets, final String haneToken) {
        for (Cluster cadet : cadets) {
            final Member member = memberService.findOne(cadet.getUser().getId())
                    .orElseThrow(MemberException.NoMemberException::new);

            if (member != null) {
                memberService.updateMemberInfo(member, haneApiService.getHaneInfo(member.getIntraName(), haneToken),
                        cadet.getUser().getLocation());
            }
            if (member == null) {
                memberService.createCadet(cadet.getUser().getId(), cadet.getUser().getLogin(),
                        haneApiService.getHaneInfo(member.getIntraName(), haneToken),
                        member.getImacLocation());
            }
        }
    }
}
