package kr.where.backend.announcement;

import java.util.List;
import kr.where.backend.announcement.dto.CreateAnnouncementDTO;
import kr.where.backend.announcement.dto.DeleteAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDTO;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private static final int PAGE_SIZE = 5;

    public ResponseAnnouncementDTO create(final CreateAnnouncementDTO createAnnouncementDto, AuthUser authUser) {
        Announcement announcement = createAnnouncementDto.toEntity(authUser);
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseAnnouncementDTO.of(savedAnnouncement);
    }

    public void delete(final DeleteAnnouncementDTO deleteAnnouncementDto) {
        Announcement announcement = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId())
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }

    public ResponseAnnouncementListDTO getAnnouncementPage(final int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Direction.DESC, "createAt"));
        List<ResponseAnnouncementDTO> responseAnnouncementDTO = announcementRepository.findAll(pageable).stream().map(
                ResponseAnnouncementDTO::of).toList();

        return ResponseAnnouncementListDTO.of(responseAnnouncementDTO);
    }

    public ResponseAnnouncementListDTO getAllAnnouncement() {
        List<Announcement> announcements = announcementRepository.findAll();

        List<ResponseAnnouncementDTO> responseAnnouncementDTOS
                = announcements.stream().map(ResponseAnnouncementDTO::of).toList();

        return ResponseAnnouncementListDTO.of(responseAnnouncementDTOS);
    }
}
