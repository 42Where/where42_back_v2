package kr.where.backend.announcement.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.announcement.dto.DeleteAnnouncementDto;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDto;
import kr.where.backend.announcement.dto.CreateAnnouncementDto;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "announcement", description = "announcement API")
public interface AnnouncementApiDocs {

    @Operation(summary = "Post announcement API", description = "공지 저장",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = CreateAnnouncementDto.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "201", description = "공지 저장 성공"),
                    @ApiResponse(responseCode = "400", description = "공지 저장 실패")
            }
    )
    @PostMapping("")
    ResponseEntity<String> saveAnnouncement(
            @RequestBody @Valid final CreateAnnouncementDto createAnnouncementDto,
            @AuthUserInfo final AuthUser authUser);
}
