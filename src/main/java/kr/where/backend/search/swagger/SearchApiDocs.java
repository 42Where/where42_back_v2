package kr.where.backend.search.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.exception.SearchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name="search", description = "search API group")
public interface SearchApiDocs {

    @Operation(summary = "search API", description = "멤버 검색 api",
            parameters = {
                    @Parameter(name="intraId", description = "검색하려는 맴버 id 값", in= ParameterIn.QUERY),
                    @Parameter(name="keyWord", description = "검색하려는 입력값", in= ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "카뎃 검색 성공", content=@Content(schema = @Schema(implementation = ResponseSearchDTO.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 입력값 오류", content=@Content(schema = @Schema(implementation = SearchException.class)))
            })
    @GetMapping("/")
    ResponseEntity<List<ResponseSearchDTO>> search42UserResponse(@RequestParam("keyWord") final String keyWord);
}
