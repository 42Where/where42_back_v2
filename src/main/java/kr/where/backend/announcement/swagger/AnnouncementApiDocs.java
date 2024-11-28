package kr.where.backend.announcement.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.announcement.dto.CreateAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementDTO;
import kr.where.backend.announcement.dto.ResponseAnnouncementListDTO;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                    content = @Content(schema = @Schema(implementation = CreateAnnouncementDTO.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지 저장 성공", content = @Content(schema = @Schema(implementation = ResponseAnnouncementDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content(schema = @Schema(type = "string")))
            }
    )
    @PostMapping("")
    ResponseEntity<ResponseAnnouncementDTO> saveAnnouncement(
            @RequestBody @Valid final CreateAnnouncementDTO createAnnouncementDto,
            @AuthUserInfo final AuthUser authUser);

    @Operation(summary = "Get announcement API", description = "페이지 단위로 조회합니다. totalPage는 \"전체 공지수 / size\"입니다. totalElements는 전체 공지수입니다.",
            parameters = {
                    @Parameter(name = "page", description = "가져올 페이지 번호 (0부터 시작)", in = ParameterIn.QUERY, required = true),
                    @Parameter(name = "size", description = "페이지 사이즈", in = ParameterIn.QUERY, required = true),
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지 반환 성공", content = @Content(schema = @Schema(implementation = ResponseAnnouncementListDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content(schema = @Schema(type = "string")))
            }
    )
    @GetMapping(value = "", params = ("page"))
    ResponseEntity<ResponseAnnouncementListDTO> getAnnouncement(
            @RequestParam("page") final Integer page,
            @RequestParam("size") final Integer size);

    @Operation(summary = "Delete announcement API", description = "공지 삭제",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "공지 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청값", content = @Content(schema = @Schema(type = "string")))
            }
    )
    @DeleteMapping("{id}")
    void deleteAnnouncement(@PathVariable("id") Long id);
}