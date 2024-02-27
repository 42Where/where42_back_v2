package kr.where.backend.update;

import java.util.ArrayList;
import java.util.List;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Cluster;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
@Transactional(readOnly = true)
public class UpdateService {
    private static final String HANE_TOKEN = "hane";
    private static final String IMAGE_TOKEN = "image";
    private static final String ADMIN_TOKEN = "admin";
    private final OAuthTokenService oauthTokenService;
    private final IntraApiService intraApiService;
    private final HaneApiService haneApiService;
    private final MemberService memberService;

    //TODO
    /**
     *
     * 1. 로그인한 모든 카뎃의 위치 업데이트 서비스
     * 2. 모든 5분마다 카뎃의 로그인 여부에 따른 위치 업데이트 서비스
     * 3. 새로운 기수 들어왔을 때, image 업데이트 서비스
     */

    /**
     * where42 서비스가 1시간 이상 다운 되었을때, 42 서울 로그인한 카뎃에 대한 위치 업데이트 42api를 호출하기 때문에 admin 토큰 호출(api 호출 제한)
     */
    @Retryable
    @Transactional
    public void updateMemberLocations() {
        final String token = oauthTokenService.findAccessToken(ADMIN_TOKEN);

        final List<Cluster> loginMember = getLoginMember(token);

        updateLocation(loginMember);
    }

    private List<Cluster> getLoginMember(final String token) {
        int page = 1;
        final List<Cluster> result = new ArrayList<>();

        while (true) {
            final List<Cluster> loginMember = intraApiService.getCadetsInCluster(token, page);
            result.addAll(loginMember);
            if (loginMember.size() < 100) {
                break;
            }
            page += 1;
        }

        return result;
    }

    private void updateLocation(final List<Cluster> cadets) {
        final String haneToken = oauthTokenService.findAccessToken(HANE_TOKEN);

        cadets.forEach(cadet -> memberService.findOne(cadet.getUser().getId())
                .ifPresent(member -> {
                    member.getLocation().setImacLocation(cadet.getUser().getLocation());
                    member.setInCluster(haneApiService.getHaneInfo(cadet.getUser().getLogin(), haneToken));
                    log.info("member {}의 클러스터 정보가 변경되었습니다", member.getIntraName());
                }));
    }

    /**
     * 5분 동안 login, logout status 적용하는 메서드
     * Hane token도 적용 해야함!
     */
    @Retryable
    @Scheduled(cron = "0 0/5 * 1/1 * ?")
    @Transactional
    public void updateMemberStatus() {
        final String token = oauthTokenService.findAccessToken(ADMIN_TOKEN);

        final List<Cluster> status = getStatus(token);

        updateLocation(status);
    }


    private List<Cluster> getStatus(final String token) {
        int page = 1;

        final List<Cluster> statusResult = new ArrayList<>();

        while(true) {
            boolean loginFlag = false;
            boolean logoutFlag = false;

            if (!loginFlag) {
                final List<Cluster> loginStatus = intraApiService.getLoginCadetsLocation(token, page);
                statusResult.addAll(loginStatus);

                if (loginStatus.size() < 100) {
                    loginFlag = true;
                }
            }
            if (!logoutFlag) {
                final List<Cluster> logoutStatus = intraApiService.getLogoutCadetsLocation(token, page);
                statusResult.addAll(logoutStatus);

                if (logoutStatus.size() < 100) {
                    logoutFlag = true;
                }
            }

            if (loginFlag && logoutFlag) {
                break;
            }

            page += 1;
        }

        return statusResult;
    }

    /**
     * 새로운 기수에 대한 image 업데이트
     */
    @Transactional
    public void updateMemberImage() {
        final String token = oauthTokenService.findAccessToken(IMAGE_TOKEN);

        final List<CadetPrivacy> cadets = getCadetsInfo(token);
        updateImage(cadets);
    }

    private List<CadetPrivacy> getCadetsInfo(final String token) {
        int page = 1;

        final List<CadetPrivacy> cadets = new ArrayList<>();
        while (true) {
            List<CadetPrivacy> response = intraApiService.getCadetsImage(token, page);
            cadets.addAll(response);

            if (response.size() < 100) {
                break;
            }
            page += 1;
        }

        return cadets;
    }

    private void updateImage(final List<CadetPrivacy> cadets) {
        cadets.forEach(cadet -> memberService.findOne(cadet.getId())
                .ifPresent(member -> member.setImage(cadet.getImage().getVersions().getSmall())));
    }
}
