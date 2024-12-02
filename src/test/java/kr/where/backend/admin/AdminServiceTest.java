package kr.where.backend.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import kr.where.backend.admin.dto.RequestAdminStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
import kr.where.backend.admin.exception.AdminException;
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
    @Autowired
    private AdminService adminService;

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
        member.setRole("ADMIN");
        memberRepository.save(member);
    }

    @DisplayName("유저 권한 조회하는 테스트")
    @Test
    @Rollback
    void getAdminStatusTest() {
        //given
        //when
        ResponseAdminStatusDTO responseAdminStatusDTO = adminService.getAdminStatus(authUser);

        //then
        assertEquals("ADMIN", responseAdminStatusDTO.getRole());
    }

    @DisplayName("유저 권한 변경 성공하는 테스트")
    @Test
    @Rollback
    void SuccessChangeAdminStatusTest() {
        Collection<? extends GrantedAuthority> authorities2 = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser2, "", authorities2));
        CadetPrivacy cadetPrivacy2 = new CadetPrivacy(222222, "jonhan", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane2 = Hane.create("IN");
        Member member2 = memberService.createAgreeMember(cadetPrivacy2, hane2);
        memberRepository.save(member2);

        //given
        RequestAdminStatusDTO requestAdminStatusDTO = new RequestAdminStatusDTO("ADMIN", "jonhan");

        //when
        ResponseAdminStatusDTO responseAdminStatusDTO = adminService.changeAdminStatus(requestAdminStatusDTO, authUser);

        //then
        assertEquals("ADMIN", responseAdminStatusDTO.getRole());
    }

    @DisplayName("유저 권한 변경 실패하는 테스트")
    @Test
    @Rollback
    void failChangeAdminStatusTest() {
        Collection<? extends GrantedAuthority> authorities2 = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser2, "", authorities2));
        CadetPrivacy cadetPrivacy2 = new CadetPrivacy(222222, "jonhan", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane2 = Hane.create("IN");
        Member member2 = memberService.createAgreeMember(cadetPrivacy2, hane2);
        memberRepository.save(member2);

        //given
        Member requester = memberRepository.findByIntraId(authUser.getIntraId()).get();
        requester.setRole("USER");
        RequestAdminStatusDTO requestAdminStatusDTO = new RequestAdminStatusDTO("ADMIN", "jonhan");

        //when
        //then
        assertThrows(AdminException.permissionDeniedException.class, () -> adminService.changeAdminStatus(requestAdminStatusDTO, authUser));
    }
}
