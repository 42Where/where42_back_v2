package kr.where.backend.version.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.version.dto.RequestVersionDTO;
import kr.where.backend.version.dto.ResponseVersionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "version", description = "version API")
public interface VersionApiDocs {

    @Operation(summary = "checkVersion API", description = "맴버 상태 메시지 변경",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RequestVersionDTO.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "현재 버전이 최신버전입니다", content = @Content(schema = @Schema(implementation = ResponseVersionDTO.class))),
                    @ApiResponse(responseCode = "426", description = "버전 업데이트가 필요합니다", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("")
    public ResponseEntity<ResponseVersionDTO> checkVersion(@RequestBody final RequestVersionDTO requestVersionDTO);

    @Operation(summary = "initVersion API", description = "DB에 IOS,ANDROID 버전 정보 없을시 버전 등록, 있으면 0.0.0으로 초기화",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "os 초기화 성공")
            }
    )
    @GetMapping("/init")
    public ResponseEntity<String> initVersion();
}
