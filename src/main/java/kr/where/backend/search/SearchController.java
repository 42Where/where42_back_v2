package kr.where.backend.search;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @GetMapping("/search")
    public List<SearchCadet> search42UserResponse(HttpServletRequest req, HttpServletResponse res,
                                                  @CookieValue(value = "ID", required = false) String key, @RequestParam("begin") String begin) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        begin = begin.toLowerCase();
        log.info("[search] \"{}\" 님이 '{}'을 검색하였습니다.", member.getName(), begin);
        List<Seoul42> searchList = apiService.get42UsersInfoInRange(token42, begin, getEnd(begin));
        List<SearchCadet> searchCadetList = new ArrayList<SearchCadet>();
        for (Seoul42 cadet : searchList) {
            SearchCadet searchCadet = searchCadetInfo(cadet.getLogin());
            if (memberRepository.checkFriendByMemberIdAndName(member.getId(), searchCadet.getName()))
                searchCadet.setFriend(true);
            searchCadetList.add(searchCadet);
        }
        return searchCadetList;
    }

    @PostMapping("/search")
    public SearchCadet getSelectCadetInfo(@RequestBody SearchCadet cadet) {
        if (cadet.isMember()) {
            Member member = memberRepository.findByName(cadet.getName());
            memberService.checkMemberStatus(member);
            cadet.updateStatus(member.getLocate(), member.getInOrOut());
        }
        else {
            if (!Define.PARSED.equalsIgnoreCase(cadet.getLocation())) {
                FlashData flash = flashDataService.findByName(cadet.getName());
                if (!Define.PARSED.equalsIgnoreCase(flash.getLocation()))
                    flashDataService.parseStatus(flash);
                cadet.updateStatus(flash.getLocate(), flash.getInOrOut());
            }
        }
        return cadet;
    }
}
