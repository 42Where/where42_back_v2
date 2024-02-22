package kr.where.backend.search;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.swagger.SearchApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/search")
@RequiredArgsConstructor
public class SearchApiController implements SearchApiDocs {

    private final SearchService searchService;

    @GetMapping("/")
    public ResponseEntity<List<ResponseSearchDTO>> search42UserResponse(@RequestParam("keyWord") final String keyWord) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseSearchDTO> responseSearchDTOS = searchService.search(keyWord, authUser);

        return ResponseEntity.ok(responseSearchDTOS);
    }
}
