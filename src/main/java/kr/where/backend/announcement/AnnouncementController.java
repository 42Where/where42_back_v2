package kr.where.backend.announcement;

import jakarta.validation.Valid;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
import kr.where.backend.announcement.swagger.AnnouncementApiDocs;
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
public class AnnouncementController implements AnnouncementApiDocs {

//    private final AnnouncementService announcementService;

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
//        announcementService.saveAnnouncement(createAnnouncementDto, authUser);
        ResponseAnnouncementDto responseAnnouncementDto = new ResponseAnnouncementDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementDto);
    }

    /**
     * 공지 페이지 단위로 조회
     *
     * @param page 쿼리 파라미터로 전달받은 페이지 번호
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping("")
    public ResponseEntity<ResponseAnnouncementListDto> getAnnouncement(@AuthUserInfo final AuthUser authUser, @RequestParam("page") final int page) {
        ResponseAnnouncementListDto responseAnnouncementListDto = new ResponseAnnouncementListDto();
//        responseAnnouncementListDto = announcementService.getAnnouncement(authUser, page);
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementListDto);
    }

    /**
     * 공지 모두 조회
     *
     * @return ResponseEntity(ResponseAnnouncementListDto)
     */
    @GetMapping("")
    public ResponseEntity<ResponseAnnouncementListDto> getAllAnnouncement(@AuthUserInfo final AuthUser authUser) {
        ResponseAnnouncementListDto responseAnnouncementListDto = new ResponseAnnouncementListDto();
//        responseAnnouncementListDto = announcementService.getAnnouncement(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementListDto);
    }

    /**
     * 공지 삭제
     *
     * @param deleteAnnouncementDto
     * @return ResponseEntity(String)
     */
    @DeleteMapping("")
    public ResponseEntity<ResponseAnnouncementDto> deleteAnnouncement(@RequestBody @Valid DeleteAnnouncementDto deleteAnnouncementDto, @AuthUserInfo final AuthUser authUser) {
//        announcementService.deleteAnnouncement(authUser, deleteAnnouncementDto);
        ResponseAnnouncementDto responseAnnouncementDto = new ResponseAnnouncementDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseAnnouncementDto);
    }
}
