package kr.where.backend.admin;

import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
import kr.where.backend.admin.swagger.AdminApiDocs;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApiDocs {
    private final MemberRepository memberRepository;

    @GetMapping("/status")
    public ResponseEntity<ResponseAdminStatusDTO> getAdminStatus(@AuthUserInfo final AuthUser authUser) {
        final ResponseAdminStatusDTO responseAdminStatusDTO = new ResponseAdminStatusDTO("USER");
        return ResponseEntity.status(HttpStatus.OK).body(responseAdminStatusDTO);
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseAdminStatusDTO> changeAdminStatus(@AuthUserInfo final AuthUser authUser) {
        final ResponseAdminStatusDTO responseAdminStatusDTO = new ResponseAdminStatusDTO("USER");
        return ResponseEntity.status(HttpStatus.OK).body(responseAdminStatusDTO);
    }
}
