package kr.where.backend.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.api.json.hane.HaneResponseDto;
import kr.where.backend.auth.authUser.AuthUser;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback
@Transactional
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    private final static Integer CAMPUS_ID = 29;

    @Test
    @DisplayName("멤버 Hane IN/OUT 상태가 업데이트 되는지 확인하는 테스트")
    public void updateMemberInOrOutStatusTest() {
        //given
        memberCreateAndSave(111111, "soohlee", "c1r1s1", "OUT", 1L);
        memberCreateAndSave(222222, "jonhan", "c1r1s1", "OUT", 2L);
        memberCreateAndSave(333333, "suhwpark", "c1r1s1", "IN", 3L);
        memberCreateAndSave(444444, "hyuim", "c1r1s1", "IN", 4L);

        final List<HaneResponseDto> haneResponse = new ArrayList<>();
        HaneResponseDto dto = new HaneResponseDto();
        ReflectionTestUtils.setField(dto, "login", "soohlee");
        ReflectionTestUtils.setField(dto, "inoutState", "IN");
        haneResponse.add(dto);

        HaneResponseDto dto1 = new HaneResponseDto();
        ReflectionTestUtils.setField(dto1, "login", "jonhan");
        ReflectionTestUtils.setField(dto1, "inoutState", "IN");
        haneResponse.add(dto1);

        HaneResponseDto dto2 = new HaneResponseDto();
        ReflectionTestUtils.setField(dto2, "login", "suhwpark");
        ReflectionTestUtils.setField(dto2, "inoutState", "OUT");
        haneResponse.add(dto2);

        HaneResponseDto dto3 = new HaneResponseDto();
        ReflectionTestUtils.setField(dto3, "login", "hyuim");
        ReflectionTestUtils.setField(dto3, "inoutState", "OUT");
        haneResponse.add(dto3);

        //when
        memberRepository.updateMemberInOrOutStatus(haneResponse);

        //then
        Member member1 = memberRepository.findByIntraName("soohlee").orElseThrow();
        Member member2 = memberRepository.findByIntraName("jonhan").orElseThrow();
        Member member3 = memberRepository.findByIntraName("suhwpark").orElseThrow();
        Member member4 = memberRepository.findByIntraName("hyuim").orElseThrow();

        assertThat(member1.isInCluster()).isEqualTo(true);
        assertThat(member2.isInCluster()).isEqualTo(true);
        assertThat(member3.isInCluster()).isEqualTo(false);
        assertThat(member4.isInCluster()).isEqualTo(false);
    }

    //멤버를 생성,저장하는 공통 메소드
    private void memberCreateAndSave(int intraId, String intraName, String location, String haneInOut, long defaultGroupId) {
        AuthUser authUser = new AuthUser(intraId, intraName, defaultGroupId);
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create(haneInOut);
        memberService.createAgreeMember(cadetPrivacy, hane);
    }
}
