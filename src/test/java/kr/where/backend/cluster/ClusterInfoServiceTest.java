package kr.where.backend.cluster;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.cluster.dto.ResponseClusterListDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClusterInfoServiceTest {

    @Autowired
    private ClusterService clusterService;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    private AuthUser authUser;

    private final static Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUP() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(11111, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(11111, "soohlee", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        member.updateRole("ADMIN");
        memberRepository.save(member);

        clusterRepository.save(Cluster.builder()
                .cluster("c1")
                .rowIndex(1)
                .seat(2)
                .member(member)
                .build());

        Collection<? extends GrantedAuthority> authorities2 = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser2, "", authorities2));
        CadetPrivacy cadetPrivacy2 = new CadetPrivacy(222222, "jonhan", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane2 = Hane.create("OUT");
        Member member2 = memberService.createAgreeMember(cadetPrivacy2, hane2);
        memberRepository.save(member2);

        clusterRepository.save(Cluster.builder()
                .cluster("c1")
                .rowIndex(4)
                .seat(5)
                .member(member2).build());
    }

    @DisplayName("imac에 로그인되어 있는 멤버를 조회하는 기능 테스트")
    @Test
    @Rollback
    void getLoginMember() {
        final ResponseClusterListDTO responseClusterListDTO = clusterService.getLoginMember(authUser, "c1");

        assertEquals(1, responseClusterListDTO.getMembers().size());
        assertEquals("soohlee", responseClusterListDTO.getMembers().get(0).getIntraName());
    }
}
