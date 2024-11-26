package kr.where.backend.announcement;

import jakarta.validation.Valid;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
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
     * @return ResponseEntity(String)
     */
    @PostMapping("")
    public ResponseEntity<ResponseAnnouncementDto> saveAnnouncement(
            @RequestBody @Valid final CreateAnnouncementDto createAnnouncementDto,
            @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.saveAnnouncement(createAnnouncementDto, authUser));
    }

    /**
     * 공지 페이지 단위로 조회
     *
     * @param page 쿼리 파라미터로 전달받은 페이지 번호
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping(params = ("page"))
    public ResponseEntity<ResponseAnnouncementListDto> getAnnouncement(
            @RequestParam("page") final int page) {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getAnnouncementPage(page));
    }

    /**
     * 공지 모두 조회
     *
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping("")
    public ResponseEntity<ResponseAnnouncementListDto> getAllAnnouncement() {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getAllAnnouncement());
    }


    /**
     * 공지 삭제
     *
     * @param deleteAnnouncementDto
     * @return ResponseEntity(String)
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteAnnouncement(
            @RequestBody @Valid final DeleteAnnouncementDto deleteAnnouncementDto) {
        announcementService.deleteAnnouncement(deleteAnnouncementDto);
        return ResponseEntity.ok().build();
    }
}
