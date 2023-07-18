package kr.where.backend.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MemberController {
    @PostMapping("/member/")
    public ResponseEntity createMember(HttpSession session, @RequestBody Seoul42 seoul42) {
        Long memberId = memberService.saveMember(seoul42.getLogin(), seoul42.getImage().getLink(), seoul42.getLocation(), seoul42.getCreated_at());
        session.setAttribute("id", memberId);
        session.setMaxInactiveInterval(60 * 60);
        return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_MEMBER, memberId), HttpStatus.CREATED);
    }

    @GetMapping("/member/info")
    public MemberInfo memberInformation(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        log.info("[main] \"{}\"님이 메인화면을 조회하였습니다.", member.getName());
        if (member.timeDiff() < 1) {
            if (!Define.PARSED.equalsIgnoreCase(member.getLocation()))
                memberService.parseStatus(member, member.getLocate().getPlanet());
            return new MemberInfo(member);
        }
        memberService.parseStatus(member, token42);
        return new MemberInfo(member);
    }

    @GetMapping("/member/status-msg")
    public String getPersonalMsg(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        return member.getMsg();
    }

    @PostMapping( "/member/status-msg")
    public ResponseEntity updatePersonalMsg(HttpServletRequest req,  HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody Map<String, String> msg) {
        String token42 = tokenService.findAccessToken(res, key);
        memberService.updatePersonalMsg(req, token42, msg.get("msg"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_MSG), HttpStatus.OK);
    }

    @GetMapping("/member/location")
    public ResponseEntity checkLocate(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key)
            throws OutStateException, TakenSeatException, ServiceUnavailableException {
        String token42 = tokenService.findAccessToken(res, key);
        int planet = memberService.checkLocate(req, token42);
        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, ResponseMsg.NOT_TAKEN_SEAT, planet), HttpStatus.OK);
    }

    @PostMapping("/member/location")
    public ResponseEntity updateLocate(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody Locate locate) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        memberService.updateLocate(member, locate);
        log.info("[setting] \"{}\"님이 \"p:{}, f:{}, c:{}, s:{}\" (으)로 위치를 수동 변경하였습니다.", member.getName(),
                locate.getPlanet(), locate.getFloor(), locate.getCluster(), locate.getSpot());
        memberService.saveLocateDate(member.getName(), locate);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_LOCATE), HttpStatus.OK);
    }

    @PostMapping("/member/eval")
    public ResponseEntity updateEvalOn(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key)
            throws OutStateException, ServiceUnavailableException {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        memberService.updateEvalOn(req, token42);
        log.info("[setting] \"{}\"님이 동료 평가 중으로 상태를 변경하였습니다.", member.getName());
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_EVAL_ON), HttpStatus.OK);
    }

    @PostMapping("/member/eval")
    public ResponseEntity updateEvalOff(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        memberService.updateEvalOff(req, token42);
        log.info("[setting] \"{}\"님이 동료 평가 상태를 해제하였습니다.", member.getName());
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_EVAL_OFF), HttpStatus.OK);
    }

    @PostMapping("member/friend")
    public ResponseEntity createFriend(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestParam String friendName, @RequestParam String img) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        if (memberRepository.checkFriendByMemberIdAndName(member.getId(), friendName))
            throw new RegisteredFriendException();
        Seoul42 friend = apiService.getUserInfo(token42, friendName);
        Long friendId = groupFriendService.saveFriend(friendName, img, friend.getCreated_at(), member.getDefaultGroupId());
        log.info("[friend] \"{}\"님이 \"{}\"님을 친구 추가 하였습니다", member.getName(), friendName);
        return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_GROUP_FRIEND, friendId), HttpStatus.CREATED);
    }

    @GetMapping("member/friend")
    public List<String> getAllDefaultFriends(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        return groupFriendRepository.findGroupFriendsByGroupId(member.getDefaultGroupId());
    }

    @DeleteMapping("/member/friend")
    public ResponseEntity deleteFriends(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody List<String> friendNames) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        groupFriendService.deleteFriends(member, friendNames);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_GROUP_FRIENDS), HttpStatus.OK);
    }
}
