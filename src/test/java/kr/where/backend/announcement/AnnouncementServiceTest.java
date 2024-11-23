package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class AnnouncementServiceTest {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepository announcementRepository;
    private AuthUser authUser;
    private final static Long INVALID_ANNOUNCEMENT_ID = 12312L;

    @BeforeEach
    public void setUP() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @DisplayName("공지를 삭제 실패하는 기능 테스트")
    @Test
    @Rollback
    public void failDeleteAnnouncementTest() {
        //given
        Announcement announcement = announcementRepository.save(new Announcement("점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now()));
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
        Announcement announcement = announcementRepository.save(new Announcement("점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now()));
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
        Announcement announcement = new Announcement("점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now());

        //when
        announcementRepository.save(announcement);

        //then
        assertEquals(announcement, announcementRepository.findById(announcement.getId()).get());
    }

    @DisplayName("공지를 모두 조회하는 기능 테스트")
    @Test
    @Rollback
    public void getAllAnnouncementTest() {
        Announcement announcementOne = new Announcement("점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementTWO = new Announcement("기능 추가 공지", "실시간 자리 확인 기능이 추가되었어요.", "soohlee", LocalDate.now(), LocalDate.now());
        Announcement announcementTHREE = new Announcement("이벤트 공지", "오늘은 이벤트가 있어요", "soohlee", LocalDate.now(), LocalDate.now());

        announcementRepository.save(announcementOne);
        announcementRepository.save(announcementTWO);
        announcementRepository.save(announcementTHREE);

        List<Announcement> announcements = announcementRepository.findAll();
        assertEquals(announcementOne, announcements.get(0));
        assertEquals(announcementTWO, announcements.get(1));
        assertEquals(announcementTHREE, announcements.get(2));
    }
}
