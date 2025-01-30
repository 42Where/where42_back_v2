package kr.where.backend.location;

import java.util.Objects;
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

    @Test
    public void update_custom_location_test() {
		//given
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(12345, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
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
        authUser = new AuthUser(12345, "suhwpark", 1L);
        agreeMemberCreateAndSave(12345, "suhwpark", "c1r1s1", "IN", authUser);

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
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        agreeMemberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        agreeMemberCreateAndSave(222222, "jonhan", "c1r1s2", "OUT", authUser2);

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser1, "c1");

        //then
        assertThat(1).isEqualTo(responseLoggedImacListDTO.getMembers().size());
    }

    @DisplayName("imac에 로그인된 멤버를 조회하는 테스트")
    @Test
    @Rollback
    void testLoggedInIMacMember() {
        // given
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        agreeMemberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser1, "c1");

        //then
        final ResponseLoggedImacDTO responseLoggedImacDTO_1 = responseLoggedImacListDTO.getMembers().stream()
                .filter(responseLoggedImacDTO -> Objects.equals("suhwpark", responseLoggedImacDTO.getIntraName()))
                .findFirst()
                .orElse(null);
        assertThat(responseLoggedImacDTO_1).isNotNull();
        assertThat("suhwpark").isEqualTo(responseLoggedImacDTO_1.getIntraName());
    }

    @DisplayName("imac에 로그인된 사람들 중 클러스터에 있는 사람만 조회하는 테스트")
    @Test
    @Rollback
    void testLoggedInIMacMemberWithoutCluster() {
        // given
        final AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        agreeMemberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        final AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        agreeMemberCreateAndSave(222222, "jonhan", "c1r1s2", "OUT", authUser2);

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser1, "c1");

        //then
        final ResponseLoggedImacDTO responseLoggedImacDTO_1 = responseLoggedImacListDTO.getMembers().stream()
                .filter(responseLoggedImacDTO -> Objects.equals("suhwpark", responseLoggedImacDTO.getIntraName()))
                .findFirst()
                .orElse(null);
        assertThat(responseLoggedImacDTO_1).isNotNull();
        assertThat("suhwpark").isEqualTo(responseLoggedImacDTO_1.getIntraName());

        final ResponseLoggedImacDTO responseLoggedImacDTO_2 = responseLoggedImacListDTO.getMembers().stream()
                .filter(responseLoggedImacDTO -> Objects.equals("jonhan", responseLoggedImacDTO.getIntraName()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(responseLoggedImacDTO_2, "jonhan is not found");
        assertEquals("jonhan", responseLoggedImacDTO_2.getIntraName());
        assertThat(responseLoggedImacDTO_2).isNull();
    }
    }

    //멤버를 생성,저장하는 공통 메소드
    private void memberCreateAndSave(int intraId, String intraName, String location, String haneInOut, AuthUser authUser) {

    //동의 멤버를 생성,저장하는 공통 메소드
    private void agreeMemberCreateAndSave(int intraId, String intraName, String location, String haneInOut, AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create(haneInOut);
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
    }

    //비동의 멤버를 생성,저장하는 공통 메소드
    private void disagreeMemberCreateAndSave(int intraId, String intraName, String location, String haneInOut, AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
        Member member = memberService.createDisagreeMember(cadetPrivacy);
    }
}
