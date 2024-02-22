package kr.where.backend.group;

import jakarta.validation.Valid;
import java.util.List;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
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
    public ResponseEntity<ResponseGroupDTO> createGroup(@RequestBody @Valid final CreateGroupDTO request) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final ResponseGroupDTO dto = groupService.createGroup(request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseGroupMemberListDTO>> findAllGroups() {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseGroupMemberListDTO> dto = groupMemberService.findMyAllGroupInformation(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/name")
    public ResponseEntity<ResponseGroupDTO> updateGroupName(@RequestBody @Valid final UpdateGroupDTO dto) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final ResponseGroupDTO responseGroupDto = groupService.updateGroup(dto, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseGroupDTO> deleteGroup(@RequestParam("groupId") final Long groupId) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final ResponseGroupDTO responseGroupDto = groupService.deleteGroup(groupId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @GetMapping("/info")
    public ResponseEntity findGroupNames() {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupsInfoByIntraId(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/groupmember")
    public ResponseEntity createGroupMember(@RequestParam("intraId") final Integer intraId) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final ResponseGroupMemberDTO dto = groupMemberService.createDefaultGroupMember(intraId, false, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/groupmember/not-ingroup")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> findMemberListNotInGroup(@RequestParam("groupId") final Long groupId) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseOneGroupMemberDTO> groupMemberDTOS = groupMemberService.findMemberNotInGroup(groupId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(groupMemberDTOS);
    }

    @PostMapping("/groupmember/members")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> addFriendsToGroup(@RequestBody @Valid final AddGroupMemberListDTO request) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseOneGroupMemberDTO> response = groupMemberService.addFriendsList(request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/groupmember")
    public ResponseEntity<List<ResponseOneGroupMemberDTO>> findAllGroupFriends(@RequestParam("groupId") final Long groupId) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseOneGroupMemberDTO> dto = groupMemberService.findGroupMemberByGroupId(groupId, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping ("/groupmember")
    public ResponseEntity removeIncludeGroupFriends(@RequestBody @Valid final DeleteGroupMemberListDTO request) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final List<ResponseGroupMemberDTO> ResponseGroupMemberDTOs = groupMemberService.deleteFriendsList(request, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseGroupMemberDTOs);
    }
}
