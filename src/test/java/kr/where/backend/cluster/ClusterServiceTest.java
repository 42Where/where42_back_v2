package kr.where.backend.cluster;

import static org.junit.Assert.assertEquals;

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
import kr.where.backend.update.UpdateService;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    public void setUP() {
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
        assertEquals(3, responseClusterListDTO.getMembers().size());

        ResponseClusterDTO responseClusterDTO_1 = responseClusterListDTO.getMembers().stream()
                .filter(responseClusterDTO -> "soohlee".equals(responseClusterDTO.getIntraName())) // "soohlee"를 찾기 위한 필터
                .findFirst() // 첫 번째로 찾은 멤버를 반환
                .orElse(null); // 없으면 null을 반환
        // "soohlee"라는 이름을 가진 멤버가 존재하면, 그 멤버의 정보가 정확한지 검증
        Assertions.assertNotNull(responseClusterDTO_1, "responseClusterDTO is null");
        // 멤버의 정보가 예상대로인지 확인
        assertEquals("soohlee", responseClusterDTO_1.getIntraName());

        ResponseClusterDTO responseClusterDTO_2 = responseClusterListDTO.getMembers().stream()
                .filter(responseClusterDTO -> "jonhan".equals(responseClusterDTO.getIntraName())) // "soohlee"를 찾기 위한 필터
                .findFirst() // 첫 번째로 찾은 멤버를 반환
                .orElse(null); // 없으면 null을 반환
        // "soohlee"라는 이름을 가진 멤버가 존재하면, 그 멤버의 정보가 정확한지 검증
        Assertions.assertNotNull(responseClusterDTO_2, "responseClusterDTO is null");
        // 멤버의 정보가 예상대로인지 확인
        assertEquals("jonhan", responseClusterDTO_2.getIntraName());
    }

    @Test
    @DisplayName("Cluster init Test")
    void clusterInitTest() {
//        clusterService.init();
        long count = clusterRepository.count();
        assertEquals(513L, count);
    }

    @Test
    @DisplayName("유저 자리사용 횟수 증가 확인 테스트")
    public void increaseUsedCountTest() {

        //given
        User user1 = new User(111111, " " , Image.create(Versions.create("")), "c1r2s2");
        User user2 = new User(222222, " " , Image.create(Versions.create("")), "c1r2s3");
        User user3 = new User(33333, " " , Image.create(Versions.create("")), "c1r2s4");

        ClusterInfo clusterInfo1 = new ClusterInfo(1, null ,LocalDateTime.now().minusMinutes(30).toString(), user1);
        ClusterInfo clusterInfo2 = new ClusterInfo(2, null ,LocalDateTime.now().minusMinutes(30).toString(), user2);
        ClusterInfo clusterInfo3 = new ClusterInfo(3, null ,LocalDateTime.now().minusMinutes(30).toString(), user3);
        List<ClusterInfo> cadet = List.of(clusterInfo1, clusterInfo2, clusterInfo3);
        updateService.updateClusterSeat(cadet);

        //when
        clusterService.increaseUsedCount("c1r2s2", 222222);

        //then
        Cluster cluster = clusterRepository.findByClusterAndRowIndexAndSeat("1",2,2)
                .orElseThrow(IllegalArgumentException::new);
        assertEquals(cluster.getUsedCount().toString(), "2");
    }
}
