package kr.where.backend.cluster;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import kr.where.backend.api.json.*;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.cluster.dto.ResponseClusterDTO;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.update.UpdateService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClusterServiceTest {

    @Autowired
    private ClusterService clusterService;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UpdateService updateService;

    private AuthUser authUser;

    private final static Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUP(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Cluster init Test")) {
            return;
        }

        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(111111, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(111111, "soohlee", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        member.updateRole("ADMIN");
        memberRepository.save(member);

        Collection<? extends GrantedAuthority> authorities2 = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser2, "", authorities2));
        CadetPrivacy cadetPrivacy2 = new CadetPrivacy(222222, "jonhan", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane2 = Hane.create("OUT");
        Member member2 = memberService.createAgreeMember(cadetPrivacy2, hane2);
        memberRepository.save(member2);

        Collection<? extends GrantedAuthority> authorities3 = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser3 = new AuthUser(33333, "jonhan", 2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser3, "", authorities3));
        CadetPrivacy cadetPrivacy3 = new CadetPrivacy(33333, "suhwpark", "c1r2s4", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane3 = Hane.create("OUT");
        memberService.createAgreeMember(cadetPrivacy3, hane3);
        clusterService.init();
    }

    @DisplayName("imac에 로그인되어 있는 멤버를 조회하는 기능 테스트")
    @Test
    @Rollback
    void getLoginMember() {
        final ResponseClusterListDTO responseClusterListDTO = clusterService.getLoginMember(authUser, "c1");
        //로그인된 아이맥 개수 확인
        assertThat(responseClusterListDTO.getMembers().size()).isEqualTo(3);

        ResponseClusterDTO responseClusterDTO_1 = responseClusterListDTO.getMembers().stream()
                .filter(responseClusterDTO -> "soohlee".equals(responseClusterDTO.getIntraName())) // "soohlee"를 찾기 위한 필터
                .findFirst() // 첫 번째로 찾은 멤버를 반환
                .orElse(null); // 없으면 null을 반환
        // "soohlee"라는 이름을 가진 멤버가 존재하면, 그 멤버의 정보가 정확한지 검증
        Assertions.assertNotNull(responseClusterDTO_1, "responseClusterDTO is null");
        // 멤버의 정보가 예상대로인지 확인
        assertThat(responseClusterDTO_1.getIntraName()).isEqualTo("soohlee");

        ResponseClusterDTO responseClusterDTO_2 = responseClusterListDTO.getMembers().stream()
                .filter(responseClusterDTO -> "jonhan".equals(responseClusterDTO.getIntraName())) // "soohlee"를 찾기 위한 필터
                .findFirst() // 첫 번째로 찾은 멤버를 반환
                .orElse(null); // 없으면 null을 반환
        // "soohlee"라는 이름을 가진 멤버가 존재하면, 그 멤버의 정보가 정확한지 검증
        Assertions.assertNotNull(responseClusterDTO_2, "responseClusterDTO is null");
        // 멤버의 정보가 예상대로인지 확인
        assertThat(responseClusterDTO_2.getIntraName()).isEqualTo("jonhan");
    }

    @Test
    @DisplayName("Cluster init Test")
    void clusterInitTest() {
        clusterService.init();
        long count = clusterRepository.count();
        assertThat(count).isEqualTo(513L);
    }

    @Test
    @DisplayName("클러스터 자리 횟수 갱신 테스트")
    public void updateClusterSeatTest() throws Exception{
        //given
        User user1 = new User(111111, " " , Image.create(Versions.create("")), "c1r2s2");
        User user2 = new User(222222, " " , Image.create(Versions.create("")), "c1r2s3");
        User user3 = new User(33333, " " , Image.create(Versions.create("")), "c1r2s4");

        ClusterInfo clusterInfo1 = new ClusterInfo(1, null ,LocalDateTime.now().minusMinutes(30).toString(), user1);
        ClusterInfo clusterInfo2 = new ClusterInfo(2, null ,LocalDateTime.now().minusMinutes(30).toString(), user2);
        ClusterInfo clusterInfo3 = new ClusterInfo(3, null ,LocalDateTime.now().minusMinutes(30).toString(), user3);
        List<ClusterInfo> cadet = List.of(clusterInfo1, clusterInfo2, clusterInfo3);

        //when
        updateService.updateClusterSeat(cadet);

        //then
        Cluster cluster1 = clusterRepository.findByClusterAndRowIndexAndSeat("1",2,2)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(cluster1.getUsedCount()).isEqualTo(1);
        Cluster cluster2 = clusterRepository.findByClusterAndRowIndexAndSeat("1",2,3)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(cluster2.getUsedCount()).isEqualTo(1);
        Cluster cluster3 = clusterRepository.findByClusterAndRowIndexAndSeat("1",2,4)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(cluster3.getUsedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("클러스터 자리 횟수 갱신 존재하지 않는 멤버 인자 테스트")
    public void updateClusterSeatNoMemberExceptionTest() throws Exception{
        //given
        final User user1 = new User(111111, " " , Image.create(Versions.create("")), "c1r2s2");
        final User user2 = new User(222222, " " , Image.create(Versions.create("")), "c1r2s3");
        final User user3 = new User(33333, " " , Image.create(Versions.create("")), "c1r2s4");
        final User user4 = new User(123456, "UnknownMember" , Image.create(Versions.create("")), "c1r2s5");

        final ClusterInfo clusterInfo1 = new ClusterInfo(1, null ,LocalDateTime.now().minusMinutes(30).toString(), user1);
        final ClusterInfo clusterInfo2 = new ClusterInfo(2, null ,LocalDateTime.now().minusMinutes(30).toString(), user2);
        final ClusterInfo clusterInfo3 = new ClusterInfo(3, null ,LocalDateTime.now().minusMinutes(30).toString(), user3);
        final ClusterInfo clusterInfo4 = new ClusterInfo(4, null ,LocalDateTime.now().minusMinutes(30).toString(), user4);
        final List<ClusterInfo> cadet = List.of(clusterInfo1, clusterInfo2, clusterInfo3, clusterInfo4);

        //then
        assertThatThrownBy(() -> updateService.updateClusterSeat(cadet))
                .isInstanceOf(MemberException.NoMemberException.class);
    }

    @Test
    @DisplayName("유저 자리사용 횟수 증가 확인 테스트")
    public void increaseUsedCountTest() {

        //given
        final User user1 = new User(111111, " " , Image.create(Versions.create("")), "c1r2s2");
        final User user2 = new User(222222, " " , Image.create(Versions.create("")), "c1r2s3");
        final User user3 = new User(33333, " " , Image.create(Versions.create("")), "c1r2s4");

        final ClusterInfo clusterInfo1 = new ClusterInfo(1, null ,LocalDateTime.now().minusMinutes(30).toString(), user1);
        final ClusterInfo clusterInfo2 = new ClusterInfo(2, null ,LocalDateTime.now().minusMinutes(30).toString(), user2);
        final ClusterInfo clusterInfo3 = new ClusterInfo(3, null ,LocalDateTime.now().minusMinutes(30).toString(), user3);
        final List<ClusterInfo> cadet = List.of(clusterInfo1, clusterInfo2, clusterInfo3);
        updateService.updateClusterSeat(cadet);

        //when
        clusterService.increaseUsedCount("c1r2s2", 222222);

        //then
        Cluster cluster = clusterRepository.findByClusterAndRowIndexAndSeat("1",2,2)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(cluster.getUsedCount()).isEqualTo(2);
    }
}
