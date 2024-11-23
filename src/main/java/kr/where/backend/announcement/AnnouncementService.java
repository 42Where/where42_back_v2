package kr.where.backend.announcement;

import java.time.LocalDate;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.announcement.exception.AnnouncementException.NoAnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public ResponseAnnouncementDto saveAnnouncement(CreateAnnouncementDto createAnnouncementDto, AuthUser authUser) {
        Announcement announcement = new Announcement(
                createAnnouncementDto.getTitle(),
                createAnnouncementDto.getComment(),
                authUser.getIntraName(),
                LocalDate.now(),
                LocalDate.now());

        announcementRepository.save(announcement);
        return ResponseAnnouncementDto.builder()
                .announcementId(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .authorName(announcement.getAuthorName())
                .createAt(announcement.getCreateAt())
                .updateAt(announcement.getUpdateAt())
                .build();
    }

    public void delete(DeleteAnnouncementDto deleteAnnouncementDto, AuthUser authUser) {
        Announcement announcement = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId())
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }
}
