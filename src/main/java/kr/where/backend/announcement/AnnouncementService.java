package kr.where.backend.announcement;

import java.util.List;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private static final int PAGE_SIZE = 5;

    public ResponseAnnouncementDto create(final CreateAnnouncementDto createAnnouncementDto, AuthUser authUser) {
        Announcement announcement = createAnnouncementDto.toEntity(authUser);
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseAnnouncementDto.of(savedAnnouncement);
    }

    public void delete(final DeleteAnnouncementDto deleteAnnouncementDto) {
        Announcement announcement = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId())
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }

    public ResponseAnnouncementListDto getAnnouncementPage(final int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        List<ResponseAnnouncementDto> responseAnnouncementDtos = announcementRepository.findAll(pageable).stream().map(
                ResponseAnnouncementDto::of).toList();

        return ResponseAnnouncementListDto.of(responseAnnouncementDtos);
    }

    public ResponseAnnouncementListDto getAllAnnouncement() {
        List<Announcement> announcements = announcementRepository.findAll();

        List<ResponseAnnouncementDto> responseAnnouncementDtos
                = announcements.stream().map(ResponseAnnouncementDto::of).toList();

        return ResponseAnnouncementListDto.of(responseAnnouncementDtos);
    }
}
