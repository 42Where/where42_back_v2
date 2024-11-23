package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.data.domain.PageRequest;
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
        assertThatThrownBy(() -> announcementService.delete(deleteAnnouncementDto, authUser)).
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
        announcementService.delete(deleteAnnouncementDto, authUser);

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

        List<Announcement> announcements = announcementRepository.findAll();
        assertEquals(announcementOne, announcements.get(0));
        assertEquals(announcementTwo, announcements.get(1));
        assertEquals(announcementThree, announcements.get(2));
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
        Page<Announcement> announcements = announcementService.getAnnouncementPage(0);
        //then
        assertEquals(5, announcements.getContent().size());
        assertEquals(announcementOne, announcements.getContent().get(0));
        assertEquals(announcementTwo, announcements.getContent().get(1));
        assertEquals(announcementThree, announcements.getContent().get(2));
        assertEquals(announcementFour, announcements.getContent().get(3));
        assertEquals(announcementFive, announcements.getContent().get(4));

        //when
        Page<Announcement> announcements2 = announcementService.getAnnouncementPage(1);
        //then
        assertEquals(1, announcements2.getContent().size());
        assertEquals(announcementSix, announcements2.getContent().get(0));
    }
}
