package kr.where.backend.analytics;

import kr.where.backend.analytics.dto.ResponseAnalyticsDTO;
import kr.where.backend.analytics.dto.ResponseImacUsageAnalyticsDTO;
import kr.where.backend.analytics.dto.ResponseMemberImacUsageAnalyticsDTO;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.config.TestRedisContainer;
import kr.where.backend.imacHistory.ImacHistory;
import kr.where.backend.imacHistory.ImacHistoryRepository;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.*;


@SpringBootTest
@Transactional
@Rollback
public class AnalyticsServiceTest {
    @Autowired
    ImacHistoryRepository imacHistoryRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    AnalyticsService analyticsService;

    @Autowired
    RedisTemplate<String, Object> template;

    static final TestRedisContainer TEST_REDIS_CONTAINER = new TestRedisContainer();
    Integer CAMPUS_ID = 29;
    AuthUser authUser;

    @BeforeAll
    public static void setContainer() {
        TEST_REDIS_CONTAINER.beforeAll();
    }

    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @AfterEach
    void flushRedis() {
        Objects.requireNonNull(template.getConnectionFactory()).getConnection().serverCommands().flushDb();
    }

    @Test
    @DisplayName("redis를 사용하여, member imac usage view table cache 존재시 cache 값 사용, 그렇지 않은 경우 생성 Test")
    void memberImacUsageAnalyticsServiceTest() throws Exception {
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
        ResponseAnalyticsDTO result = analyticsService.getMemberImacUsageAnalyticsData(intraId, 1);

        //then
        assertThat(result.getSeats().size()).isEqualTo(1);

        ResponseMemberImacUsageAnalyticsDTO dto = (ResponseMemberImacUsageAnalyticsDTO) result.getSeats().get(0);
        assertThat(dto.getUsingCount()).isEqualTo(3);
        assertThat(dto.getSeat()).isEqualTo("c1r1s1");
        assertThat(dto.getUsingTimeHour()).isEqualTo(9);
        assertThat(dto.getUsingTimeMinute()).isEqualTo(0);
        assertThat(dto.getUsingTimeSecond()).isEqualTo(0);
    }

    @Test
    @DisplayName("redis를 사용하여, imac usage view table cache 존재시 cache 값 사용, 그렇지 않은 경우 생성 Test")
    void imacUsageAnalyticsServiceTest() throws Exception {
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
        ResponseAnalyticsDTO result = analyticsService.getImacUsageAnalyticsData(1);

        //then
        assertThat(result.getSeats().size()).isEqualTo(1);

        ResponseImacUsageAnalyticsDTO dto = (ResponseImacUsageAnalyticsDTO) result.getSeats().get(0);
        assertThat(dto.getUsingUserCount()).isEqualTo(3);
        assertThat(dto.getSeat()).isEqualTo("c1r1s1");
        assertThat(dto.getUsingTimeHour()).isEqualTo(9);
        assertThat(dto.getUsingTimeMinute()).isEqualTo(0);
        assertThat(dto.getUsingTimeSecond()).isEqualTo(0);

    }


    private String[] getUtcTimeString(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String loginUtc = start.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);
        String logoutUtc = end.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC).format(utcFormatter);

        return new String[]{loginUtc, logoutUtc};
    }
}
