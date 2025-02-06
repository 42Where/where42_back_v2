package kr.where.backend.search;

import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.swagger.SearchApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;

@RestController
@RequestMapping("/v3/search")
@RequiredArgsConstructor
@RequestLogging(level = LogLevel.INFO)
public class SearchController implements SearchApiDocs {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<List<ResponseSearchDTO>> search42UserResponse(
            @RequestParam("keyWord") final String keyWord,
            @AuthUserInfo final AuthUser authUser) {

        return ResponseEntity.ok(searchService.search(keyWord, authUser));
    }

    @GetMapping("/new")
    public ResponseEntity<List<ResponseSearchDTO>> searchUser(@RequestParam("keyWord") final String keyWord,
                                                              @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.ok().body(searchService.searchUser(keyWord, authUser));
    }

}
