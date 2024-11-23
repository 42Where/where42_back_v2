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
    private final AnnouncementRepository announcementRepository;

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
        ResponseAnnouncementDto responseAnnouncementDto = announcementService.saveAnnouncement(createAnnouncementDto, authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementDto);
    }

    /**
     * 공지 페이지 단위로 조회
     *
     * @param page 쿼리 파라미터로 전달받은 페이지 번호
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping(params = ("page"))
    public ResponseEntity<ResponseAnnouncementListDto> getAnnouncement(
            @AuthUserInfo final AuthUser authUser,
            @RequestParam("page") final int page) {
        ResponseAnnouncementListDto responseAnnouncementListDto = new ResponseAnnouncementListDto();
//        responseAnnouncementListDto = announcementService.getAnnouncement(authUser, page);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseAnnouncementListDto);
    }

    /**
     * 공지 모두 조회
     *
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping("")
    public ResponseEntity<ResponseAnnouncementListDto> getAllAnnouncement(
            @AuthUserInfo final AuthUser authUser) {
        ResponseAnnouncementListDto responseAnnouncementListDto = announcementService.getAllAnnouncement(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementListDto);
    }


    /**
     * 공지 삭제
     *
     * @param deleteAnnouncementDto
     * @return ResponseEntity(String)
     */
    @DeleteMapping("")
    public ResponseEntity<ResponseAnnouncementDto> deleteAnnouncement(
            @RequestBody @Valid DeleteAnnouncementDto deleteAnnouncementDto,
            @AuthUserInfo final AuthUser authUser) {
        //Delete 요청시 응답바디가 없어야 할것 같아 응답값 변경을 회의시 제안할 예정입니다. 그리하여 아래 코드는 임시 응답값만을 위한 임시 코드입니다.
        Announcement announcement = announcementRepository.findById(deleteAnnouncementDto.getAnnouncementId()).get();
        ResponseAnnouncementDto responseAnnouncementDto = ResponseAnnouncementDto.builder().
                announcementId(announcement.getId()).
                title(announcement.getTitle()).
                content(announcement.getContent()).
                createAt(announcement.getCreateAt()).
                updateAt(announcement.getUpdateAt()).
                build();

        announcementService.deleteAnnouncement(deleteAnnouncementDto, authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementDto);
    }
}
