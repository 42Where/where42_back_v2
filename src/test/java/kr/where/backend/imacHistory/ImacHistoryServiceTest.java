package kr.where.backend.imacHistory;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.imacHistory.dto.GroupByImac;
import kr.where.backend.imacHistory.exception.ImacHistoryException;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

@SpringBootTest
@Transactional
@Rollback
public class ImacHistoryServiceTest {
    @Autowired
    ImacHistoryService imacHistoryService;

    @Autowired
    ImacHistoryRepository imacHistoryRepository;

    @Autowired
    MemberService memberService;

    AuthUser authUser;
    Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @Test
    @DisplayName("imac history 저장 test")
    void saveImacHistoryTest() {
        //given

        //member create
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1",
                "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after3Hour = now.plusHours(3);

        String[] utcTimes = getUtcTimeString(now, after3Hour);
        Long id = imacHistoryService.create(member.getIntraId(), "c1r1s1", utcTimes[0], utcTimes[1]);
        //when
        ImacHistory imacHistory = imacHistoryRepository.findById(id).
                orElseThrow(ImacHistoryException.NoImacHistoryException::new);

        //then
        assertThat(id).isEqualTo(imacHistory.getId());
        assertThat("c1r1s1").isEqualTo(imacHistory.getImac());
        assertThat(now.withNano(0)).isEqualTo(imacHistory.getLoginAt());
        assertThat(after3Hour.withNano(0)).isEqualTo(imacHistory.getLogoutAt());
    }

    @Test
    @DisplayName("각 member 별 imac history 사용 기록 및 시간 구하는 test")
    void getGroupedByImacHistoryTest() {
        //given
        //member create
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
                new ImacHistory(intraId, "c5r8s8", utcTimes[0], utcTimes[1])
        );

        imacHistoryRepository.saveAll(imacHistories);
        //when
        long startNative = System.currentTimeMillis();
        List<GroupByImac> groupByImacsByNativeQuery = imacHistoryService.getTotalUsageTimeByImac(intraId);
        long endNative = System.currentTimeMillis() - startNative;

        System.out.println("getTotalUsageTimeByImac : " + endNative + "ms");

        long startJpa = System.currentTimeMillis();
        List<GroupByImac> groupByImacsByJpa = imacHistoryService.getTotalUsageTimeByJpa(intraId);
        long endJpa = System.currentTimeMillis() - startJpa;

        System.out.println("getTotalUsageTimeByJpa : " + endJpa + "ms");

        groupByImacsByNativeQuery.sort((o1, o2) -> (int) (o2.getUsageTime() - o1.getUsageTime()));
        groupByImacsByJpa.sort((o1, o2) -> (int) (o2.getUsageTime() - o1.getUsageTime()));
        //then
        System.out.println(groupByImacsByNativeQuery);
        System.out.println(groupByImacsByJpa);


        //nativeQuery result : 평균 21ms (findAllGroupByImac), 메서드 실행 : 23ms (getTotalUsageTimeByImac)
        assertThat(groupByImacsByNativeQuery.size()).isEqualTo(3);

        assertThat(groupByImacsByNativeQuery.get(0).getImac()).isEqualTo("c1r1s1");
        assertThat(groupByImacsByNativeQuery.get(1).getImac()).isEqualTo("cx1r2s4");
        assertThat(groupByImacsByNativeQuery.get(2).getImac()).isEqualTo("c5r8s8");

        assertThat(groupByImacsByNativeQuery.get(0).getCount()).isEqualTo(3);
        assertThat(groupByImacsByNativeQuery.get(1).getCount()).isEqualTo(2);
        assertThat(groupByImacsByNativeQuery.get(2).getCount()).isEqualTo(1);

        assertThat(groupByImacsByNativeQuery.get(0).getUsageTime()).isEqualTo(32400L);
        assertThat(groupByImacsByNativeQuery.get(1).getUsageTime()).isEqualTo(21600L);
        assertThat(groupByImacsByNativeQuery.get(2).getUsageTime()).isEqualTo(10800L);

//         Jpql Result : 평균 2 ~ 3ms (finaAllByIntraId), 메서드 실행 : 5ms (getTotalUsageTimeByJpa)
        assertThat(groupByImacsByJpa.size()).isEqualTo(3);

        assertThat(groupByImacsByJpa.get(0).getImac()).isEqualTo("c1r1s1");
        assertThat(groupByImacsByJpa.get(1).getImac()).isEqualTo("cx1r2s4");
        assertThat(groupByImacsByJpa.get(2).getImac()).isEqualTo("c5r8s8");

        assertThat(groupByImacsByJpa.get(0).getCount()).isEqualTo(3);
        assertThat(groupByImacsByJpa.get(1).getCount()).isEqualTo(2);
        assertThat(groupByImacsByJpa.get(2).getCount()).isEqualTo(1);

        assertThat(groupByImacsByJpa.get(0).getUsageTime()).isEqualTo(32400L);
        assertThat(groupByImacsByJpa.get(1).getUsageTime()).isEqualTo(21600L);
        assertThat(groupByImacsByJpa.get(2).getUsageTime()).isEqualTo(10800L);
    }

    private String[] getUtcTimeString(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String loginUtc = start.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);
        String logoutUtc = end.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);

        return new String[]{loginUtc, logoutUtc};
    }
}
