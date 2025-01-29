package kr.where.backend.location;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.*;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.Assertions;
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

import static org.assertj.core.api.AssertionsForClassTypes.*;
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
        memberCreateAndSave(12345, "suhwpark", "c1r1s1", "IN", authUser);

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
        memberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        memberCreateAndSave(222222, "jonhan", "c1r1s2", "OUT", authUser2);

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser1, "c1");

        //then
        assertEquals(2, responseLoggedImacListDTO.getMembers().size());
    }

    @DisplayName("imac에 로그인된 멤버 이름을 확인하는 테스트")
    @Test
    @Rollback
    void testLoggedInIMacMemberNames() {
        // given
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        memberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 2L);
        memberCreateAndSave(222222, "jonhan", "c1r1s2", "OUT", authUser2);

        // when
        final ResponseLoggedImacListDTO responseLoggedImacListDTO = locationService.getLoggedInIMacs(authUser1, "c1");

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

    @Test
    @DisplayName("클러스터 별 아이맥 사용량 조회 테스트")
    void getClusterUsageTest() {
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        memberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 3L);
        memberCreateAndSave(222222, "jonhan", null, "IN", authUser2);
        AuthUser authUser3 = new AuthUser(333333, "soohlee", 4L);
        memberCreateAndSave(333333, "soohlee", "c2r1s1", "IN", authUser3);
        AuthUser authUser4 = new AuthUser(133456, "daejlee", 2L);
        memberCreateAndSave(133456, "daejlee", "c1r1s2", "IN", authUser4);

        //1클러스터 총 인원과 현재 인원 체크
        ResponseClusterUsageListDTO responseClusterUsageListDTO = locationService.getClusterImacUsage();
        assertThat(responseClusterUsageListDTO.getClusters().get(0).getTotalImacCount()).isEqualTo(63);
        assertThat(responseClusterUsageListDTO.getClusters().get(0).getUsingImacCount()).isEqualTo(2);
        assertThat(responseClusterUsageListDTO.getClusters().get(0).getUsageRate()).isEqualTo(3);

        //2클러스터 총 인원과 현재 인원 체크
        assertThat(responseClusterUsageListDTO.getClusters().get(1).getTotalImacCount()).isEqualTo(80);
        assertThat(responseClusterUsageListDTO.getClusters().get(1).getUsingImacCount()).isEqualTo(1);
        assertThat(responseClusterUsageListDTO.getClusters().get(1).getUsageRate()).isEqualTo(1);
    }

    @Test
    @DisplayName("출근 인원 대비 아이맥 사용자 비율 계산 시 0 나누기 예외 테스트")
    void getImacUsageDoesNotThrowExceptionTest() {
        //inCluster 유저 0명으로 셋팅
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        memberCreateAndSave(123456, "suhwpark", null, "OUT", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 3L);
        memberCreateAndSave(222222, "jonhan", null, "OUT", authUser2);
        AuthUser authUser3 = new AuthUser(333333, "soohlee", 4L);
        memberCreateAndSave(333333, "soohlee", null, "OUT", authUser3);
        AuthUser authUser4 = new AuthUser(133456, "daejlee", 2L);
        memberCreateAndSave(133456, "daejlee", null, "OUT", authUser4);

        assertThatCode(() -> locationService.getImacUsagePerHaneCount()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("출근 인원 대비 아이맥 사용자 비율 조회 테스트")
    void getImacUsageTest() {
        AuthUser authUser1 = new AuthUser(123456, "suhwpark", 2L);
        memberCreateAndSave(123456, "suhwpark", "c1r1s1", "IN", authUser1);
        AuthUser authUser2 = new AuthUser(222222, "jonhan", 3L);
        memberCreateAndSave(222222, "jonhan", null, "IN", authUser2);
        AuthUser authUser3 = new AuthUser(333333, "soohlee", 4L);
        memberCreateAndSave(333333, "soohlee", "c2r1s1", "IN", authUser3);
        AuthUser authUser4 = new AuthUser(133456, "daejlee", 2L);
        memberCreateAndSave(133456, "daejlee", null, "OUT", authUser4);

        ResponseImacUsageDTO responseImacUsageDTO = locationService.getImacUsagePerHaneCount();

        assertThat(responseImacUsageDTO.getTotalUserCount()).isEqualTo(3);
        assertThat(responseImacUsageDTO.getUsingImacUserCount()).isEqualTo(2);
        assertThat(responseImacUsageDTO.getUsageRate()).isEqualTo(66);
    }

    //멤버를 생성,저장하는 공통 메소드
    private void memberCreateAndSave(int intraId, String intraName, String location, String haneInOut, AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create(haneInOut);
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
    }
}
