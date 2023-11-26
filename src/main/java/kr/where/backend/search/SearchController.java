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
import kr.where.backend.utils.response.ResponseWithData;
import kr.where.backend.utils.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name="search", description = "search API group")
@RequestMapping("/v3/search")
public class SearchController {

    @Operation(summary = "search API", description = "멤버 검색 api",
            parameters = {
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE),
                    @Parameter(name="begin", description = "begin 인자로 시작하는 이름", in= ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "카뎃 검색 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class))),
                    @ApiResponse(responseCode = "401", description = "등록되지 않은 멤버", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/")
    public ResponseEntity search42UserResponse(HttpServletRequest req, HttpServletResponse res,
                                                  @CookieValue(value = "ID", required = false) String key, @RequestParam("begin") String begin) {
//        String token42 = tokenService.findAccessToken(res, key);
//        Member member = memberService.findBySessionWithToken(req, token42);
//        begin = begin.toLowerCase();
//        log.info("[search] \"{}\" 님이 '{}'을 검색하였습니다.", member.getName(), begin);
//        List<Seoul42> searchList = apiService.get42UsersInfoInRange(token42, begin, getEnd(begin));
//        List<SearchCadet> searchCadetList = new ArrayList<SearchCadet>();
//        for (Seoul42 cadet : searchList) {
//            SearchCadet searchCadet = searchCadetInfo(cadet.getLogin());
//            if (memberRepository.checkFriendByMemberIdAndName(member.getId(), searchCadet.getName()))
//                searchCadet.setFriend(true);
//            searchCadetList.add(searchCadet);
//        }
        List<SearchCadet> searchCadetList = new ArrayList<SearchCadet>();
        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, "검색 성공", searchCadetList), HttpStatus.OK);
//        return searchCadetList;
    }

    @Operation(summary = "getSelectCadetInfo API", description = "검색된 카뎃 선택시 정보 파싱",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content( schema = @Schema(implementation = SearchCadet.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "get 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class)))
            })
    @PostMapping("/select")
    public ResponseEntity getSelectCadetInfo(@RequestBody SearchCadet cadet) {
//        if (cadet.isMember()) {
//            Member member = memberRepository.findByName(cadet.getName());
//            memberService.checkMemberStatus(member);
//            cadet.updateStatus(member.getLocate(), member.getInOrOut());
//        }
//        else {
//            if (!Define.PARSED.equalsIgnoreCase(cadet.getLocation())) {
//                FlashData flash = flashDataService.findByName(cadet.getName());
//                if (!Define.PARSED.equalsIgnoreCase(flash.getLocation()))
//                    flashDataService.parseStatus(flash);
//                cadet.updateStatus(flash.getLocate(), flash.getInOrOut());
//            }
//        }
//        return cadet;
          return new ResponseEntity(ResponseWithData.res(StatusCode.OK, "get 성공", cadet), HttpStatus.OK);
    }
}
