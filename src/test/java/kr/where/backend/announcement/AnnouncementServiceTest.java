package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
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

    @DisplayName("공지를 삭제 실패하는 기능 테스트")
    @Test
    @Rollback
    public void failDeleteAnnouncementTest() {
        //given
        Announcement announcement = announcementRepository.save(new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now()));
        DeleteAnnouncementDto deleteAnnouncementDto = new DeleteAnnouncementDto(INVALID_ANNOUNCEMENT_ID);

        //when
        //then
        assertThatThrownBy(() -> announcementService.deleteAnnouncement(deleteAnnouncementDto)).
                isInstanceOf(AnnouncementException.NoAnnouncementException.class);
    }

    @DisplayName("공지를 삭제 성공하는 기능 테스트")
    @Test
    @Rollback
    public void successDeleteAnnouncementTest() {
        //given
        Announcement announcement = announcementRepository.save(new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now()));
        DeleteAnnouncementDto deleteAnnouncementDto = new DeleteAnnouncementDto(announcement.getId());

        //when
        announcementService.deleteAnnouncement(deleteAnnouncementDto);

        //then
        Optional<Announcement> deletedAnnouncementDto = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId());
        assertThat(deletedAnnouncementDto.isEmpty());
    }

    @DisplayName("공지를 추가하는 기능 테스트")
    @Test
    @Rollback
    public void addAnnouncementTest() {
        //given
        Announcement announcement = new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now());

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
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementTwo = new Announcement(
                "기능 추가 공지", "실시간 자리 확인 기능이 추가되었어요.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementThree = new Announcement(
                "이벤트 공지", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());

        announcementRepository.save(announcementOne);
        announcementRepository.save(announcementTwo);
        announcementRepository.save(announcementThree);

        ResponseAnnouncementListDto responseAnnouncementListDto = announcementService.getAllAnnouncement();
        List<ResponseAnnouncementDto> responseAnnouncementDtos = responseAnnouncementListDto.getResponseAnnouncementDto();
        assertEquals(announcementOne.getId(), responseAnnouncementDtos.get(0).getAnnouncementId());
        assertEquals(announcementTwo.getId(), responseAnnouncementDtos.get(1).getAnnouncementId());
        assertEquals(announcementThree.getId(), responseAnnouncementDtos.get(2).getAnnouncementId());
    }

    @DisplayName("페이지 번호로 공지 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAnnouncementTest() {
        //given
        Announcement announcementOne = new Announcement(
                "점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementTwo = new Announcement(
                "기능 추가 공지", "실시간 자리 확인 기능이 추가되었어요.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementThree = new Announcement(
                "이벤트 공지", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementFour = new Announcement(
                "이벤트 공지 4", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementFive = new Announcement(
                "이벤트 공지 5", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementSix = new Announcement(
                "이벤트 공지 6", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());

        announcementRepository.save(announcementOne);
        announcementRepository.save(announcementTwo);
        announcementRepository.save(announcementThree);
        announcementRepository.save(announcementFour);
        announcementRepository.save(announcementFive);
        announcementRepository.save(announcementSix);

        //when
        ResponseAnnouncementListDto responseAnnouncementListDto = announcementService.getAnnouncementPage(0);
        List<ResponseAnnouncementDto> responseAnnouncementDtos = responseAnnouncementListDto.getResponseAnnouncementDto();
        //then
        assertEquals(5, responseAnnouncementDtos.size());
        assertEquals(announcementOne.getId(), responseAnnouncementDtos.get(0).getAnnouncementId());
        assertEquals(announcementTwo.getId(), responseAnnouncementDtos.get(1).getAnnouncementId());
        assertEquals(announcementThree.getId(), responseAnnouncementDtos.get(2).getAnnouncementId());
        assertEquals(announcementFour.getId(), responseAnnouncementDtos.get(3).getAnnouncementId());
        assertEquals(announcementFive.getId(), responseAnnouncementDtos.get(4).getAnnouncementId());

        //when
        ResponseAnnouncementListDto responseAnnouncementListDto2 = announcementService.getAnnouncementPage(1);
        List<ResponseAnnouncementDto> responseAnnouncementDtos2 = responseAnnouncementListDto2.getResponseAnnouncementDto();
        //then
        assertEquals(1, responseAnnouncementDtos2.size());
        assertEquals(announcementSix.getId(), responseAnnouncementDtos2.get(0).getAnnouncementId());
    }
}
