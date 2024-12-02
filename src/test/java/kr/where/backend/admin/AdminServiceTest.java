package kr.where.backend.admin;

import static org.junit.Assert.assertEquals;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import kr.where.backend.admin.dto.RequestAdminStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
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

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class AdminServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    private final static Integer CAMPUS_ID = 29;

    private AuthUser authUser;

    @BeforeEach
    public void setUP() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(11111, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(11111, "soohlee", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        memberRepository.save(member);
    }

    @DisplayName("유저 권한 확인하는 테스트")
    @Test
    @Rollback
    void getAdminStatusTest() {
        ResponseAdminStatusDTO responseAdminStatusDTO = adminService.getAdminStatus(authUser);

        assertEquals("user", responseAdminStatusDTO.getRole());
    }

    @DisplayName("유저 권한 변경하는 테스트")
    @Test
    @Rollback
    void changeAdminStatusTest() {
        RequestAdminStatusDTO requestAdminStatusDTO = new RequestAdminStatusDTO("admin");
        ResponseAdminStatusDTO responseAdminStatusDTO = adminService.changeAdminStatus(requestAdminStatusDTO, authUser);

        assertEquals(requestAdminStatusDTO.getRole(), responseAdminStatusDTO.getRole());
    }
}
