package kr.where.backend.announcement;

import java.time.LocalDate;
import java.util.List;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseAnnouncementDto.builder()
                .announcementId(savedAnnouncement.getId())
                .title(savedAnnouncement.getTitle())
                .content(savedAnnouncement.getContent())
                .authorName(savedAnnouncement.getAuthorName())
                .createAt(savedAnnouncement.getCreateAt())
                .updateAt(savedAnnouncement.getUpdateAt())
                .build();
    }

    public void deleteAnnouncement(DeleteAnnouncementDto deleteAnnouncementDto, AuthUser authUser) {
        Announcement announcement = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId())
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }

    public ResponseAnnouncementListDto getAnnouncementPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);
        List<ResponseAnnouncementDto> responseAnnouncementDtos = announcementRepository.findAll(pageable).stream().map(
                announcement -> ResponseAnnouncementDto.builder().
                        announcementId(announcement.getId()).
                        title(announcement.getTitle()).
                        content(announcement.getContent()).
                        createAt(announcement.getCreateAt()).
                        updateAt(announcement.getUpdateAt()).
                        build()).toList();

        return new ResponseAnnouncementListDto(responseAnnouncementDtos);
    }

    public ResponseAnnouncementListDto getAllAnnouncement(AuthUser authUser) {
        List<Announcement> announcements = announcementRepository.findAll();

        List<ResponseAnnouncementDto> responseAnnouncementDtos
                = announcements.stream().map(announcement -> ResponseAnnouncementDto.builder().
                        announcementId(announcement.getId()).
                        title(announcement.getTitle()).
                        content(announcement.getContent()).
                        createAt(announcement.getCreateAt()).
                        updateAt(announcement.getUpdateAt()).
                        build()).toList();

        return new ResponseAnnouncementListDto(responseAnnouncementDtos);
    }
}
