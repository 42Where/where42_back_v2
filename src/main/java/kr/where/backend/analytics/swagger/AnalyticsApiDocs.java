package kr.where.backend.analytics.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.analytics.dto.ResponsePopularImacListDTO;
import kr.where.backend.analytics.dto.ResponseSeatHistoryListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "analytics", description = "analytics API")
public interface AnalyticsApiDocs {

    @Operation(
            summary = "get most frequently seated seat API",
            description = "내가 가장 자주 앉는 아이맥 자리",
            parameters = {
                    @Parameter(name = "count", description = "반환 받고 싶은 자리 갯수 (default = 1)", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseSeatHistoryListDTO.class)))
            }
    )
    @GetMapping("/seat-history")
    ResponseEntity<ResponseSeatHistoryListDTO> getMemberSeatHistory(@RequestParam("count") final Integer count);


    @Operation(
            summary = "get most Popular Imac API",
            description = "클러스터 내 가장 인기 있는 자리",
            parameters = {
                    @Parameter(name = "count", description = "반환 받고 싶은 아이맥 갯수 (default = 5)", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseSeatHistoryListDTO.class)))
            }
    )
    @GetMapping("/popular-imac")
    ResponseEntity<ResponsePopularImacListDTO> getPopularImac(@RequestParam("count") final Integer count);
}
