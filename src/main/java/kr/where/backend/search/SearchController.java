package kr.where.backend.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.exception.SearchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="search", description = "search API group")
@RequestMapping("/v3/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "search API", description = "멤버 검색 api",
            parameters = {
                    @Parameter(name="intraId", description = "검색하려는 맴버 id 값", in= ParameterIn.QUERY),
                    @Parameter(name="keyWord", description = "검색하려는 입력값", in= ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "카뎃 검색 성공", content=@Content(schema = @Schema(implementation = ResponseSearchDTO.class))),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 입력값 오류", content=@Content(schema = @Schema(implementation = SearchException.class)))
            })
    @GetMapping("")
    public ResponseEntity search42UserResponse(
            @RequestParam("keyWord") final String keyWord,
            @AuthUserInfo final AuthUser authUser) {
        final List<ResponseSearchDTO> responseSearchDTOS = searchService.search(keyWord, authUser);

        return ResponseEntity.ok(responseSearchDTOS);
    }
}
