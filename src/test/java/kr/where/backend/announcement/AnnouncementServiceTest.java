package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

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
    void failDeleteAnnouncementTest() {
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
    void successDeleteAnnouncementTest() {
        //given
        Announcement announcement = announcementRepository.save(new Announcement("점검 공지", "오늘 10시 ~ 12시까지 점검입니다.", "soohlee", LocalDate.now(), LocalDate.now()));
        DeleteAnnouncementDto deleteAnnouncementDto = new DeleteAnnouncementDto(announcement.getId());

        //when
        announcementService.delete(deleteAnnouncementDto, authUser);

        //then
        Optional<Announcement> deletedAnnouncementDto = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId());
        assertThat(deletedAnnouncementDto.isEmpty());
    }

}
