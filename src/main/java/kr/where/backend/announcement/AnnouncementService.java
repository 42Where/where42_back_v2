package kr.where.backend.announcement;

import java.util.List;
import kr.where.backend.announcement.dto.CreateAnnouncementDTO;
import kr.where.backend.announcement.dto.RequestPaginationDTO;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private static final String CREATE_AT = "createAt";

    @Transactional
    public ResponseAnnouncementDTO create(final CreateAnnouncementDTO createAnnouncementDto, final AuthUser authUser) {
        final Announcement announcement = createAnnouncementDto.toEntity(authUser);
        final Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseAnnouncementDTO.of(savedAnnouncement);
    }

    @Transactional
    public void delete(final Long announcementId) {
        final Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(AnnouncementException.NoAnnouncementException::new);
        announcementRepository.delete(announcement);
    }

    public ResponseAnnouncementListDTO getAnnouncementPage(final RequestPaginationDTO requestPaginationDTO) {
        final Page<Announcement> announcements = announcementRepository.findAll(
                PageRequest.of(requestPaginationDTO.getPage(), requestPaginationDTO.getSize(), Sort.by(Direction.DESC, CREATE_AT)));

        final List<ResponseAnnouncementDTO> responseAnnouncementDTO = announcements.stream().map(
                ResponseAnnouncementDTO::of).toList();
        return ResponseAnnouncementListDTO.of(responseAnnouncementDTO, announcements.getTotalPages(), announcements.getTotalElements());
    }
}
