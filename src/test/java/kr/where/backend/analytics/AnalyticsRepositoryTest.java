package kr.where.backend.analytics;

import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsRepository;
import kr.where.backend.analytics.imacUsageAnalytics.ImacUsageAnalyticsView;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsRepository;
import kr.where.backend.analytics.memberImacUsageAnalytics.MemberImacUsageAnalyticsView;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.imacHistory.ImacHistory;
import kr.where.backend.imacHistory.ImacHistoryRepository;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
@Transactional
@Rollback
public class AnalyticsRepositoryTest {
    @Autowired
    MemberImacUsageAnalyticsRepository memberImacUsageAnalyticsRepository;

    @Autowired
    ImacUsageAnalyticsRepository imacUsageAnalyticsRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    ImacHistoryRepository imacHistoryRepository;

    Integer CAMPUS_ID = 29;
    AuthUser authUser;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }


    @Test
    @DisplayName("member 별 imac 사용 자리에 대한 view 테이블 생성 테스트")
    void memberImacUsageViewRepositoryTest() throws Exception {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        Integer intraId= member.getIntraId();

        LocalDateTime present = LocalDateTime.now();
        LocalDateTime after3Hour = present.plusHours(3);
        String[] utcTimes = getUtcTimeString(present, after3Hour);

        List<ImacHistory> imacHistories = List.of(
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "cx1r2s4", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "cx1r2s4", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c5r8s8", utcTimes[0], utcTimes[1]),
                new ImacHistory(12345, "c5r8s8", utcTimes[0], utcTimes[1])
        );
        imacHistoryRepository.saveAll(imacHistories);

        //when
        List<MemberImacUsageAnalyticsView> memberImacUsageAnalyticsViewsIntraId =
                memberImacUsageAnalyticsRepository.findAllBy(intraId);
        List<MemberImacUsageAnalyticsView> memberImacUsageAnalyticsViews12345 =
                memberImacUsageAnalyticsRepository.findAllBy(12345);
        //then
        assertThat(memberImacUsageAnalyticsViewsIntraId.size()).isEqualTo(3);
        for (int i = 0; i < 3; ++i) {
            MemberImacUsageAnalyticsView data = memberImacUsageAnalyticsViewsIntraId.get(i);
            if (data.getImac().equals("c1r1s1")) {
                assertThat(data.getUsageTime()).isEqualTo(32400L);
                assertThat(data.getUsageCount()).isEqualTo(3);
            }
            if (data.getImac().equals("cx1r2s4")) {
                assertThat(data.getUsageTime()).isEqualTo(21600L);
                assertThat(data.getUsageCount()).isEqualTo(2);
            }
            if (data.getImac().equals("c5r8s8")) {
                assertThat(data.getUsageTime()).isEqualTo(10800L);
                assertThat(data.getUsageCount()).isEqualTo(1);
            }
        }

        assertThat(memberImacUsageAnalyticsViews12345.size()).isEqualTo(1);
        assertThat(memberImacUsageAnalyticsViews12345.get(0).getImac()).isEqualTo("c5r8s8");
        assertThat(memberImacUsageAnalyticsViews12345.get(0).getUsageTime()).isEqualTo(10800L);
        assertThat(memberImacUsageAnalyticsViews12345.get(0).getUsageCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("imac 자리별 사용에 대한 view 테이블 생성 test")
    void imacUsageViewRepositoryTest() throws Exception {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        Integer intraId= member.getIntraId();

        LocalDateTime present = LocalDateTime.now();
        LocalDateTime after3Hour = present.plusHours(3);
        String[] utcTimes = getUtcTimeString(present, after3Hour);

        List<ImacHistory> imacHistories = List.of(
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c1r1s1", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "cx1r2s4", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "cx1r2s4", utcTimes[0], utcTimes[1]),
                new ImacHistory(intraId, "c5r8s8", utcTimes[0], utcTimes[1]),
                new ImacHistory(12345, "c5r8s8", utcTimes[0], utcTimes[1])
        );
        imacHistoryRepository.saveAll(imacHistories);

        //when
        List<ImacUsageAnalyticsView> imacUsageAnalyticsViews = imacUsageAnalyticsRepository.findAll();

        //then
        assertThat(imacUsageAnalyticsViews.size()).isEqualTo(3);
        for (int i = 0; i < 3; ++i) {
            ImacUsageAnalyticsView data = imacUsageAnalyticsViews.get(i);
            if (data.getImac().equals("c1r1s1")) {
                assertThat(data.getUsageTime()).isEqualTo(32400L);
                assertThat(data.getUsageCount()).isEqualTo(3);
            }
            if (data.getImac().equals("cx1r2s4")) {
                assertThat(data.getUsageTime()).isEqualTo(21600L);
                assertThat(data.getUsageCount()).isEqualTo(2);
            }
            if (data.getImac().equals("c5r8s8")) {
                assertThat(data.getUsageTime()).isEqualTo(21600L);
                assertThat(data.getUsageCount()).isEqualTo(2);
            }
        }
    }
    private String[] getUtcTimeString(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String loginUtc = start.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);
        String logoutUtc = end.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);

        return new String[]{loginUtc, logoutUtc};
    }
}
