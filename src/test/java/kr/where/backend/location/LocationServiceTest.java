package kr.where.backend.location;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.ResponseLoggedImacDTO;
import kr.where.backend.location.dto.ResponseLoggedImacListDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;


@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    private AuthUser authUser;

    private final static Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(12345, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }
    @Test
    public void update_custom_location_test() {
		//given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", 29);
        Hane hane = Hane.create("IN");

        Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);

        //when
        UpdateCustomLocationDTO updateCustomLocationDto = UpdateCustomLocationDTO.createForTest("1F open lounge");
        ResponseLocationDTO responseLocationDto = locationService.updateCustomLocation(updateCustomLocationDto, authUser);

        Location location = locationRepository.findByMember(agreeMember);
        Optional<Member> member = memberRepository.findByIntraId(agreeMember.getIntraId());

        //then
        assertThat(responseLocationDto.getIntraId()).isEqualTo(agreeMember.getIntraId());
        assertThat(responseLocationDto.getCustomLocation()).isEqualTo(location.getCustomLocation());
        assertThat(responseLocationDto.getCustomUpdatedAt()).isEqualTo(location.getCustomUpdatedAt());
        assertThat(responseLocationDto.getImacLocation()).isEqualTo(location.getImacLocation());
        assertThat(responseLocationDto.getImacUpdatedAt()).isEqualTo(location.getImacUpdatedAt());
        assertThat(member.get().getLocation().getLocation()).isEqualTo(location.getLocation());
        assertThat(location.getLocation()).isEqualTo("1F open lounge");
    }

    @Test
    public void delete_custom_location_test() {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", 29);
        Hane hane = Hane.create("IN");

        memberService.createAgreeMember(cadetPrivacy, hane);

        UpdateCustomLocationDTO updateCustomLocationDto = UpdateCustomLocationDTO.createForTest("1F open lounge");
        ResponseLocationDTO beforeResponseLocationDTO = locationService.updateCustomLocation(updateCustomLocationDto, authUser);

        //when
        ResponseLocationDTO afterResponseLocationDTO = locationService.deleteCustomLocation(authUser);

        //then
        assertThat(beforeResponseLocationDTO.getCustomLocation()).isEqualTo("1F open lounge");
        assertThat(afterResponseLocationDTO.getCustomLocation()).isEqualTo(null);
    }

    @DisplayName("imac에 로그인된 멤버 수를 확인하는 테스트")
    @Test
    @Rollback
    void testLoggedInIMacCount() {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
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

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser, "c1");

        //then
        assertEquals(2, responseLoggedImacListDTO.getMembers().size());
    }

    @DisplayName("imac에 로그인된 멤버 이름을 확인하는 테스트")
    @Test
    @Rollback
    void testLoggedInIMacMemberNames() {
        // given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
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

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser, "c1");

        //then
        ResponseLoggedImacDTO responseLoggedImacDTO_1 = responseLoggedImacListDTO.getMembers().stream()
                .filter(responseLoggedImacDTO -> "suhwpark".equals(responseLoggedImacDTO.getIntraName()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(responseLoggedImacDTO_1, "suhwpark is not found");
        assertEquals("suhwpark", responseLoggedImacDTO_1.getIntraName());
        ResponseLoggedImacDTO responseLoggedImacDTO_2 = responseLoggedImacListDTO.getMembers().stream()
                .filter(responseLoggedImacDTO -> "jonhan".equals(responseLoggedImacDTO.getIntraName()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(responseLoggedImacDTO_2, "jonhan is not found");
        assertEquals("jonhan", responseLoggedImacDTO_2.getIntraName());
    }
}
