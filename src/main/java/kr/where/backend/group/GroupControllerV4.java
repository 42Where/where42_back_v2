package kr.where.backend.group;


import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.group.dto.group.ResponseOwnGroupMemberDTO;
import kr.where.backend.group.swagger.GroupApiDocsV4;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v4/group")
@AllArgsConstructor
@RequestLogging(level = LogLevel.INFO)
public class GroupControllerV4 implements GroupApiDocsV4 {

    private final GroupMemberService groupMemberService;

    @GetMapping("")
    public ResponseEntity<ResponseOwnGroupMemberDTO> findOwnGroups(@AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.ok().body(groupMemberService.getOwnGroups(authUser));
    }
}
