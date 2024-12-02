package kr.where.backend.admin;

import jakarta.validation.Valid;
import kr.where.backend.admin.dto.RequestAdminStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
import kr.where.backend.admin.swagger.AdminApiDocs;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApiDocs {
    private final MemberService memberService;

    @GetMapping("/status")
    public ResponseEntity<ResponseAdminStatusDTO> getAdminStatus(@AuthUserInfo final AuthUser authUser) {
        final ResponseAdminStatusDTO responseAdminStatusDTO = new ResponseAdminStatusDTO("USER");
//        final ResponseAdminStatusDTO responseAdminStatusDTO = adminService.getAdminStatus(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAdminStatusDTO);
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseAdminStatusDTO> changeAdminStatus(@RequestBody @Valid RequestAdminStatusDTO requestAdminStatusDTO,
                                                                    @AuthUserInfo final AuthUser authUser) {
        final ResponseAdminStatusDTO responseAdminStatusDTO = new ResponseAdminStatusDTO("USER");
//        final ResponseAdminStatusDTO responseAdminStatusDTO = adminService.changeAdminStatus(requestAdminStatusDTO, authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseAdminStatusDTO);
    }
}
