package kr.where.backend.seatHistory.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.seatHistory.dto.ResponseHistoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "seat-history", description = "seat-history API")
public interface SeatHistoryApiDocs {
    @Operation(summary = "getPopularSeatHistory API", description = "본인이 가장 선호하는 자리 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseHistoryDTO.class)))
            }
    )
    @GetMapping("")
    ResponseEntity<List<ResponseHistoryDTO>> getPopularSeatHistory(final @AuthUserInfo AuthUser authUser);
}
