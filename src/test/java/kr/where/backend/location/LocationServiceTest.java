//package kr.where.backend.location;
//
//import kr.where.backend.api.json.CadetPrivacy;
//import kr.where.backend.api.json.Hane;
//import kr.where.backend.location.dto.ResponseLocationDto;
//import kr.where.backend.location.dto.UpdateCustomLocationDto;
//import kr.where.backend.member.Member;
//import kr.where.backend.member.MemberRepository;
//import kr.where.backend.member.MemberService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@Rollback
//public class LocationServiceTest {
//
//    @Autowired
//    private LocationService locationService;
//    @Autowired
//    private LocationRepository locationRepository;
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    public void update_custom_location_test() {
//		//given
//        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
//        Hane hane = Hane.createForTest("IN");
//
//        Member agreeMember = memberService.createAgreeMember(cadetPrivacy, hane);
//
//        //when
//        UpdateCustomLocationDto updateCustomLocationDto = UpdateCustomLocationDto.createForTest(agreeMember.getIntraId(), "1F open lounge");
//        ResponseLocationDto responseLocationDto = locationService.updateCustomLocation(updateCustomLocationDto);
//
//        Location location = locationRepository.findByMember(agreeMember);
//        Optional<Member> member = memberRepository.findByIntraId(agreeMember.getIntraId());
//
//        //then
//        assertThat(responseLocationDto.getIntraId()).isEqualTo(agreeMember.getIntraId());
//        assertThat(responseLocationDto.getCustomLocation()).isEqualTo(location.getCustomLocation());
//        assertThat(responseLocationDto.getCustomUpdatedAt()).isEqualTo(location.getCustomUpdatedAt());
//        assertThat(responseLocationDto.getImacLocation()).isEqualTo(location.getImacLocation());
//        assertThat(responseLocationDto.getImacUpdatedAt()).isEqualTo(location.getImacUpdatedAt());
//        assertThat(member.get().getLocation().getLocation()).isEqualTo(location.getLocation());
//        assertThat(location.getLocation()).isEqualTo("1F open lounge");
//    }
//}
