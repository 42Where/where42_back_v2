package kr.where.backend.announcement;

import jakarta.validation.Valid;
import kr.where.backend.announcement.dto.RequestPaginationDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDTO;
import kr.where.backend.announcement.dto.CreateAnnouncementDTO;
import kr.where.backend.announcement.swagger.AnnouncementApiDocs;
import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/announcement")
@RequiredArgsConstructor
@RequestLogging(level = LogLevel.INFO)
public class AnnouncementController implements AnnouncementApiDocs {
    private final AnnouncementService announcementService;

    /**
     * 공지 저장
     *
     * @param createAnnouncementDto
     * @return ResponseEntity(ResponseAnnouncementDTO)
     */
    @PostMapping("")
    public ResponseEntity<ResponseAnnouncementDTO> saveAnnouncement(
            @RequestBody @Valid final CreateAnnouncementDTO createAnnouncementDto,
            @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.create(createAnnouncementDto, authUser));
    }

    /**
     * 공지 페이지 단위로 조회
     *
     * @return ResponseEntity(ResponseAnnouncementListDTO)
     */
    @GetMapping()
    public ResponseEntity<ResponseAnnouncementListDTO> getAnnouncement(@Valid @ModelAttribute RequestPaginationDTO requestPaginationDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getAnnouncementPage(requestPaginationDTO));
    }

    /**
     * 공지 삭제
     *
     * @return void
     */
    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable ("id") Long id) {
        announcementService.delete(id);
    }
}
