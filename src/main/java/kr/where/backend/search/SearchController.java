package kr.where.backend.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.utils.response.ResponseWithData;
import kr.where.backend.utils.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name="search", description = "search API group")
@RequestMapping("/v3/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "search API", description = "멤버 검색 api",
            parameters = {
                    @Parameter(name="keyWord", description = "검색하려는 입력값", in= ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "카뎃 검색 성공", content=@Content(schema = @Schema(implementation = ResponseSearch.class))),
                    @ApiResponse(responseCode = "401", description = "입력값 오류", content=@Content(schema = @Schema(implementation = SearchException.class)))
            })
    @GetMapping("/")
    public ResponseEntity search42UserResponse(@RequestParam("keyWord") String keyWord) {
        final List<ResponseSearch> responseSearches = searchService.search(keyWord);

        return ResponseEntity.ok(responseSearches);
    }
}
