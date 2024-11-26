package kr.where.backend.announcement;

import java.util.List;
import kr.where.backend.announcement.dto.CreateAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDTO;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private static final String CREATE_AT = "createAt";

    public ResponseAnnouncementDTO create(final CreateAnnouncementDTO createAnnouncementDto, final AuthUser authUser) {
        final Announcement announcement = createAnnouncementDto.toEntity(authUser);
        final Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseAnnouncementDTO.of(savedAnnouncement);
    }

    public void delete(final Long announcementId) {
        final Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }

    public ResponseAnnouncementListDTO getAnnouncement(final Integer pageNumber, final Integer size) {
        if (pageNumber == null || size == null) {
            return getAllAnnouncement();
        }
        return getAnnouncementPage(pageNumber, size);
    }

    public ResponseAnnouncementListDTO getAllAnnouncement() {
        final List<Announcement> announcements = announcementRepository.findAllByOrderByCreateAtDesc();

        final List<ResponseAnnouncementDTO> responseAnnouncementDTOS
                = announcements.stream().map(ResponseAnnouncementDTO::of).toList();

        return ResponseAnnouncementListDTO.of(responseAnnouncementDTOS);
    }

    public ResponseAnnouncementListDTO getAnnouncementPage(final Integer pageNumber, final Integer size) {

        final Page<Announcement> announcements = announcementRepository.findAll(
                PageRequest.of(pageNumber, size, Sort.by(Direction.DESC, CREATE_AT))
        );

        final int totalPages = announcements.getTotalPages();
        final long totalElements = announcements.getTotalElements();

        final List<ResponseAnnouncementDTO> responseAnnouncementDTO = announcements.stream().map(
                ResponseAnnouncementDTO::of).toList();
        return ResponseAnnouncementListDTO.of(responseAnnouncementDTO, totalPages, totalElements);
    }
}
