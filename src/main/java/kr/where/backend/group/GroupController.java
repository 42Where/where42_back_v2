package kr.where.backend.group;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class GroupController {

    @GetMapping("/group")
    public List<MemberGroupInfo> memberGroupInformation(@RequestParam Long id) {
        Member member = memberService.findById(id);
        return memberService.findAllGroupFriendsInfo(member);
    }

    @PostMapping("/group")
    public ResponseEntity createCustomGroup(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestParam("groupName") String groupName) {
        Member owner = groupService.findOwnerBySessionWithToken(req, res, key);
        Long groupId = groupService.createCustomGroup(groupName, owner);
        log.info("[group] '{}'님이 그룹을 생성하였습니다.", owner.getName());
        return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_GROUP, groupId), HttpStatus.CREATED);
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity updateGroupName(@PathVariable("groupId") Long groupId, @RequestParam("changeName") String changeName) {
        groupService.updateGroupName(groupId, changeName);
        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, ResponseMsg.CHANGE_GROUP_NAME, groupId), HttpStatus.OK);
    }

    @DeleteMapping(Define.WHERE42_VERSION_PATH + "/group/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteByGroupId(groupId);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_GROUP), HttpStatus.OK);
    }

    @GetMapping("/groupFriend/not-nclude/")
    public List<String> getNotIncludeGroupFriendNames(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @PathVariable("groupId") Long groupId) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        List<String> ret = groupFriendRepository.notIncludeFriendByGroup(member, groupId);
        if (ret == null)
            throw new BadRequestException();
        return ret;
    }

    @PostMapping("/group/{groupId}/friend")
    public ResponseEntity addFriendsToGroup(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
        groupFriendService.addFriendsToGroup(friendNames, groupId);
        log.info("[group] {}번 그룹에 친구가 추가되었습니다.", groupId);
        return new ResponseEntity(Response.res(StatusCode.CREATED, ResponseMsg.ADD_FRIENDS_TO_GROUP), HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}/friend")
    public List<String> getIncludeGroupFriendNames(@PathVariable("groupId") Long groupId) {
        List<String> ret = groupFriendRepository.findGroupFriendsByGroupId(groupId);
        if (ret == null)
            throw new BadRequestException();
        return ret;
    }

    @DeleteMapping("/group/{groupId}/friend")
    public ResponseEntity removeIncludeGroupFriends(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
        groupFriendService.deleteIncludeGroupFriends(friendNames, groupId);
        log.info("[group] {}번 그룹에서 친구가 삭제되었습니다.", groupId);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_FRIENDS_FROM_GROUP), HttpStatus.OK);
    }

}
