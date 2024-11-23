package kr.where.backend.announcement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.exception.AnnouncementException;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class AnnouncementServiceTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;
    @DisplayName("공지를 삭제 실패하는 기능 테스트")
    @Test
    @Rollback
    void deleteAnnouncementTest() {
        //given
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        AuthUser authUser = new AuthUser(135436, "soohlee", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        announcementRepository.save(new Announcement("a", "a", "a", LocalDate.now(), LocalDate.now()));

        //when
        DeleteAnnouncementDto deleteAnnouncementDto = new DeleteAnnouncementDto(1L);

        //then
        assertThatThrownBy(() -> announcementService.delete(deleteAnnouncementDto, authUser)).
                isInstanceOf(AnnouncementException.NoAnnouncementException.class);
    }

}
