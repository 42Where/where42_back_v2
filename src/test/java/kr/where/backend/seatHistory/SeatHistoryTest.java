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

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class SeatHistoryTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SeatHistoryRepository seatHistoryRepository;

    @Autowired
    private SeatHistoryService seatHistoryService;

    private AuthUser authUser;

    private final static Integer CAMPUS_ID = 29;
    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(12345, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
    }

    @Test
    @DisplayName("맴버가 자리에 로그인 했을 때, 로그인 한 자리 정보 추가")
    void createSeatHistory() {
        //given
        String seat = "c1r2s1";

        //when
        Long id = seatHistoryService.create(seat, 12345);

        //then
        SeatHistory seatHistory = seatHistoryRepository.findById(id)
                .orElseThrow(SeatHistoryException.NoSeatHistoryException::new);
        Member member = memberRepository.findByIntraId(12345)
                .orElseThrow(MemberException.NoMemberException::new);

        List<SeatHistory> seatHistoryList = member.getSeatHistories();

        assertThat(seatHistoryList.contains(seatHistory)).isTrue();
    }

}
