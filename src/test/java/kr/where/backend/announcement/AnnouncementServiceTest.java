package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import kr.where.backend.announcement.dto.ResponseAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDTO;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
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

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class AnnouncementServiceTest {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepository announcementRepository;
    private AuthUser authUser;
    private final static Long INVALID_ANNOUNCEMENT_ID = 12312L;
    private final static int PAGE_SIZE = 5;
    private final static int PAGE_NUMBER = 1;

    @BeforeEach
    public void setUP() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }
//
//    @DisplayName("공지를 삭제 실패하는 기능 테스트")
//    @Test
//    @Rollback
//    public void failDeleteAnnouncementTest() {
//        //given
//        Announcement announcement = announcementRepository.save(new Announcement(
//                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDateTime.now(), LocalDateTime.now()));
//        DeleteAnnouncementDTO deleteAnnouncementDto = new DeleteAnnouncementDTO(INVALID_ANNOUNCEMENT_ID);
//
//        //when
//        //then
//        assertThatThrownBy(() -> announcementService.delete(deleteAnnouncementDto)).
//                isInstanceOf(AnnouncementException.NoAnnouncementException.class);
//    }

//    @DisplayName("공지를 삭제 성공하는 기능 테스트")
//    @Test
//    @Rollback
//    public void successDeleteAnnouncementTest() {
//        //given
//        Announcement announcement = announcementRepository.save(new Announcement(
//                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDateTime.now(), LocalDateTime.now()));
//        DeleteAnnouncementDTO deleteAnnouncementDto = new DeleteAnnouncementDTO(announcement.getId());
//
//        //when
//        announcementService.delete(deleteAnnouncementDto);
//
//        //then
//        Optional<Announcement> deletedAnnouncementDto = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId());
//        assertThat(deletedAnnouncementDto.isEmpty());
//    }

    @DisplayName("공지를 추가하는 기능 테스트")
    @Test
    @Rollback
    public void addAnnouncementTest() {
        //given
        Announcement announcement = new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDateTime.now(), LocalDateTime.now());

        //when
        announcementRepository.save(announcement);

        //then
        assertEquals(announcement, announcementRepository.findById(announcement.getId()).get());
    }

    @DisplayName("공지를 모두 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAllAnnouncementTest() {
        Announcement announcementOne = new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementTwo = new Announcement(
                "기능 추가 공지", "실시간 자리 확인 기능이 추가되었어요.", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementThree = new Announcement(
                "이벤트 공지", "오늘은 이벤트가 있어요", "soohlee", LocalDateTime.now(), LocalDateTime.now());

        announcementRepository.save(announcementOne);
        announcementRepository.save(announcementTwo);
        announcementRepository.save(announcementThree);

        ResponseAnnouncementListDTO responseAnnouncementListDto = announcementService.getAllAnnouncement();
        List<ResponseAnnouncementDTO> responseAnnouncementDTO = responseAnnouncementListDto.getAnnouncements();
        assertEquals(announcementThree.getId(), responseAnnouncementDTO.get(0).getAnnouncementId());
        assertEquals(announcementTwo.getId(), responseAnnouncementDTO.get(1).getAnnouncementId());
        assertEquals(announcementOne.getId(), responseAnnouncementDTO.get(2).getAnnouncementId());
    }

    @DisplayName("공지가 없을때 공지 모두 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAllAnnouncementWhenEmptyTest() {

        ResponseAnnouncementListDTO responseAnnouncementListDto = announcementService.getAllAnnouncement();
        List<ResponseAnnouncementDTO> responseAnnouncementDTO = responseAnnouncementListDto.getAnnouncements();

        assertTrue(responseAnnouncementDTO.isEmpty());
    }

    @DisplayName("페이지 번호로 공지 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAnnouncementPageTest() {
        //given
        Announcement announcementOne = new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementTwo = new Announcement(
                "기능 추가 공지", "실시간 자리 확인 기능이 추가되었어요.", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementThree = new Announcement(
                "이벤트 공지", "오늘은 이벤트가 있어요", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementFour = new Announcement(
                "이벤트 공지 4", "오늘은 이벤트가 있어요", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementFive = new Announcement(
                "이벤트 공지 5", "오늘은 이벤트가 있어요", "soohlee", LocalDateTime.now(), LocalDateTime.now());
        Announcement announcementSix = new Announcement(
                "이벤트 공지 6", "오늘은 이벤트가 있어요", "soohlee", LocalDateTime.now(), LocalDateTime.now());

        announcementRepository.save(announcementOne);
        announcementRepository.save(announcementTwo);
        announcementRepository.save(announcementThree);
        announcementRepository.save(announcementFour);
        announcementRepository.save(announcementFive);
        announcementRepository.save(announcementSix);

        //when
        ResponseAnnouncementListDTO responseAnnouncementListDto = announcementService.getAnnouncementPage(0, 5);
        List<ResponseAnnouncementDTO> ResponseAnnouncementDTO = responseAnnouncementListDto.getAnnouncements();
        //then
        assertEquals(5, ResponseAnnouncementDTO.size());
        assertEquals(announcementSix.getId(), ResponseAnnouncementDTO.get(0).getAnnouncementId());
        assertEquals(announcementFive.getId(), ResponseAnnouncementDTO.get(1).getAnnouncementId());
        assertEquals(announcementFour.getId(), ResponseAnnouncementDTO.get(2).getAnnouncementId());
        assertEquals(announcementThree.getId(), ResponseAnnouncementDTO.get(3).getAnnouncementId());
        assertEquals(announcementTwo.getId(), ResponseAnnouncementDTO.get(4).getAnnouncementId());

        //when
        ResponseAnnouncementListDTO responseAnnouncementListDTO2 = announcementService.getAnnouncementPage(1, 5);
        List<ResponseAnnouncementDTO> responseAnnouncementDtos2 = responseAnnouncementListDTO2.getAnnouncements();
        //then
        assertEquals(1, responseAnnouncementDtos2.size());
        assertEquals(announcementOne.getId(), responseAnnouncementDtos2.get(0).getAnnouncementId());
    }

    @DisplayName("공지가 없을때 페이지 번호로 공지 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAnnouncementPageWhenEmptyTest() {
        //when
        ResponseAnnouncementListDTO responseAnnouncementListDto = announcementService.getAnnouncementPage(0, 5);
        List<ResponseAnnouncementDTO> responseAnnouncementDTO = responseAnnouncementListDto.getAnnouncements();
        //then
        assertTrue(responseAnnouncementDTO.isEmpty());
    }
}
