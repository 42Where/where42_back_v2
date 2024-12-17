package kr.where.backend.seatHistory;

import com.fasterxml.classmate.MemberResolver;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.seatHistory.exception.SeatHistoryException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class SeatHistoryTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SeatHistoryRepository seatHistoryRepository;

    @Autowired
    SeatHistoryService seatHistoryService;

    private final static Integer CAMPUS_ID = 29;
    @BeforeEach
    void setUp() {
        CadetPrivacy cadetPrivacy = new CadetPrivacy(11111, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
    }

    @Test
    @DisplayName("맴버가 자리에 로그인 했을 때, 로그인 한 자리 정보 추가")
    void createSeatHistory() {
        //given
        String seat = "c1r2s1";

        //when
        Long id = seatHistoryService(seat, 111111);

        //then
        SeatHistory seatHistory = seatHistoryRepository.findById(id)
                .orElseThrow(SeatHistoryException.NoSeatHistoryException::new);
        Member member = memberRepository.findByIntraId(11111).orElseThrow(MemberException.NoMemberException::new)
        List<SeatHistory> seatHistoryList = member.getSeatHistory();

        assertThat(seatHistoryList.contains(seatHistory)).isTrue();
    }

}
