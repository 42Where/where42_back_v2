//package kr.where.backend.seatHistory;
//
//import kr.where.backend.api.json.CadetPrivacy;
//import kr.where.backend.api.json.hane.Hane;
//import kr.where.backend.auth.authUser.AuthUser;
//import kr.where.backend.member.Member;
//import kr.where.backend.member.MemberRepository;
//import kr.where.backend.member.MemberService;
//import kr.where.backend.member.exception.MemberException;
//import kr.where.backend.seatHistory.dto.ResponseHistoryDTO;
//import kr.where.backend.seatHistory.exception.SeatHistoryException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collection;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//
//@SpringBootTest
//@Transactional
//@Rollback
//@ActiveProfiles("test")
//public class SeatHistoryTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private SeatHistoryRepository seatHistoryRepository;
//
//    @Autowired
//    private SeatHistoryService seatHistoryService;
//
//    private AuthUser authUser;
//
//    private final static Integer CAMPUS_ID = 29;
//    @BeforeEach
//    void setUp() {
//        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
//        authUser = new AuthUser(12345, "suhwpark", 1L);
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
//
//        memberService.createAgreeMember(
//                new CadetPrivacy(12345, "suhwpark", "c1r1s1", "image", true, "2022-10-31", CAMPUS_ID),
//                Hane.create("IN")
//        );
//    }
//
//    @Test
//    @DisplayName("맴버가 자리에 로그인 했을 때, 로그인 한 자리 정보 추가하는 test")
//    void createSeatHistory() {
//        //given
//        String seat = "c1r2s1";
//
//        //when
//        Long id = seatHistoryService.create(seat, 12345);
//
//        //then
//        SeatHistory seatHistory = seatHistoryRepository.findById(id)
//                .orElseThrow(SeatHistoryException.NoSeatHistoryException::new);
//        Member member = memberRepository.findByIntraId(12345)
//                .orElseThrow(MemberException.NoMemberException::new);
//
//        List<SeatHistory> seatHistoryList = member.getSeatHistories();
//
//        assertThat(seatHistoryList.contains(seatHistory)).isTrue();
//        assertThat(seatHistoryList.size()).isEqualTo(1);
//    }
//
//    /**
//     * 주의사항:
//     * - 테스트는 SeatHistory::increaseCount 메서드가 항상 사용 횟수를 증가시키는 상태에서 실행되어야 한다.
//     * 5분 제한 조건이 없는 상태에서 테스트 수행하기 위해 SeatHistory::increaseCount함수에 `if (!isAfter5Minute()) {return}`을 주석처리 하고 실행한다.
//     */
//    @Test
//    @DisplayName("사용했던 자리면, 사용한 수를 증가하고 처음 앉는 자리라면 새로 만들어서 기록하는 test")
//    void createOrIncreaseHistory() {
//        //given
//        List<String> seats = List.of("c1r1s1", "cx2r2s4", "c1r5s7", "c6r5s9", "c2r2s2", "c1r1s1");
//
//        //when
//        for (String seat : seats) {
//            seatHistoryService.report(seat, 12345);
//        }
//
//        //then
//        Member member = memberRepository.findByIntraId(12345).orElseThrow(MemberException.NoMemberException::new);
//
//        assertThat(member.getSeatHistories().size()).isEqualTo(5);
//        SeatHistory seatHistory = member.getSeatHistories().stream()
//                .filter(s -> "c1r1s1".equals(s.getImac()))
//                .findFirst()
//                .orElse(null);
//        assertThat(seatHistory.getCount()).isEqualTo(2);
//    }
//
//    /**
//     * 주의사항:
//     * - 테스트는 SeatHistory::increaseCount 메서드가 항상 사용 횟수를 증가시키는 상태에서 실행되어야 한다.
//     * 5분 제한 조건이 없는 상태에서 테스트 수행하기 위해 SeatHistory::increaseCount함수에 `if (!isAfter5Minute()) {return}`을 주석처리 하고 실행한다.
//     */
//    @Test
//    @DisplayName("가장 많이 사용하는 자리 3곳과 총 사용자리 대비 퍼센트 구하는 test")
//    void getPopularSeatHistories() {
//        //given
//        List<String> seats = List.of("c1r1s1", "cx2r2s4", "c1r5s7", "c1r1s1", "cx2r2s4", "c1r1s1");
//        for (String seat : seats) {
//            seatHistoryService.report(seat, 12345);
//        }
//        //when
//        List<ResponseHistoryDTO> seatHistories = seatHistoryService.getPopularSeatHistory(authUser.getIntraId());
//
//        //then
//        assertThat(seatHistories.size()).isEqualTo(3);
//        assertThat(seatHistories.get(0).getImac()).isEqualTo("c1r1s1");
//        assertThat(seatHistories.get(0).getPercent()).isEqualTo(50.0);
//        assertThat(seatHistories.get(1).getImac()).isEqualTo("cx2r2s4");
//        assertThat(seatHistories.get(1).getPercent()).isEqualTo(33.3);
//        assertThat(seatHistories.get(2).getImac()).isEqualTo("c1r5s7");
//        assertThat(seatHistories.get(2).getPercent()).isEqualTo(16.7);
//    }
//
//    @Test
//    @DisplayName("업데이트 한지 5분 이상 지났을 경우 판단하는 Test")
//    void isAfterFiveMinute() {
//        //given
//        List<String> seats = List.of("c1r1s1", "c1r1s1");
//
//        for (String s : seats) {
//            seatHistoryService.report(s, 12345);
//        }
//
//        //when
//        List<SeatHistory> seatHistories = seatHistoryRepository
//                .findALLByMemberIntraId(12345);
//
//        //then
//        assertThat(seatHistories.size()).isEqualTo(1);
//        assertThat(seatHistories.get(0).getImac()).isEqualTo("c1r1s1");
//    }
//
//    @Test
//    @DisplayName("자리 정보 추가시 잘못된 존재하지 intra_id를 저장하려고 할때 익센션 터지는 Test")
//    void saveInvalidIntraIdThrowsException() {
//        assertThrows(MemberException.NoMemberException.class, () -> seatHistoryService.report("c1r1s1", 99999999));
//    }
//}
