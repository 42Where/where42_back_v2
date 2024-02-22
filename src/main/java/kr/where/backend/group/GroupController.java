package kr.where.backend.group;

import jakarta.validation.Valid;
import java.util.List;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.dto.group.UpdateGroupDTO;
import kr.where.backend.group.swagger.GroupApiDocs;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/group")
@AllArgsConstructor
public class GroupController implements GroupApiDocs {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    @PostMapping("")
    public ResponseEntity<ResponseGroupDTO> createGroup(
            @RequestBody @Valid final CreateGroupDTO request,
            @AuthUserInfo final AuthUser authUser) {
        final ResponseGroupDTO dto = groupService.createGroup(request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    //완성

    @GetMapping("")
    public ResponseEntity<List<ResponseGroupMemberListDTO>> findAllGroups(@AuthUserInfo final AuthUser authUser) {
        final List<ResponseGroupMemberListDTO> dto = groupMemberService.findMyAllGroupInformation(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/name")
    public ResponseEntity<ResponseGroupDTO> updateGroupName(
            @RequestBody @Valid final UpdateGroupDTO dto,
            @AuthUserInfo final AuthUser authUser) {
        final ResponseGroupDTO responseGroupDto = groupService.updateGroup(dto, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseGroupDTO> deleteGroup(
            @RequestParam("groupId") final Long groupId,
            @AuthUserInfo final AuthUser authUser) {
        final ResponseGroupDTO responseGroupDto = groupService.deleteGroup(groupId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @GetMapping("/info")
    public ResponseEntity<List<ResponseGroupMemberDTO>> findGroupNames(@AuthUserInfo final AuthUser authUser) {
        final List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupsInfoByIntraId(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/groupmember")
    public ResponseEntity createGroupMember(
            @RequestParam("intraId") final Integer intraId,
            @AuthUserInfo final AuthUser authUser) {
        final ResponseGroupMemberDTO dto = groupMemberService.createDefaultGroupMember(intraId, false, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/groupmember/not-ingroup")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> findMemberListNotInGroup(
            @RequestParam("groupId") final Long groupId,
            @AuthUserInfo final AuthUser authUser) {
        final List<ResponseOneGroupMemberDTO> groupMemberDTOS = groupMemberService.findMemberNotInGroup(groupId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(groupMemberDTOS);
    }

    @PostMapping("/groupmember/members")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> addFriendsToGroup(
            @RequestBody @Valid final AddGroupMemberListDTO request,
            @AuthUserInfo final AuthUser authUser) {
        final List<ResponseOneGroupMemberDTO> response = groupMemberService.addFriendsList(request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/groupmember")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> findAllGroupFriends(
            @RequestParam("groupId") final Long groupId,
            @AuthUserInfo final AuthUser authUser) {
        final List<ResponseOneGroupMemberDTO> dto = groupMemberService.findGroupMemberByGroupId(groupId, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping ("/groupmember")
    public ResponseEntity removeIncludeGroupFriends(
            @RequestBody @Valid final DeleteGroupMemberListDTO request,
            @AuthUserInfo final AuthUser authUser) {
        final List<ResponseGroupMemberDTO> ResponseGroupMemberDTOs = groupMemberService.deleteFriendsList(request, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseGroupMemberDTOs);
    }
}
