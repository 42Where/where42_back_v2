package kr.where.backend.admin;

import jakarta.validation.Valid;
import kr.where.backend.admin.dto.RequestRoleStatusDTO;
import kr.where.backend.admin.dto.ResponseRoleStatusDTO;
import kr.where.backend.admin.swagger.AdminApiDocs;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
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
    private final AdminService adminService;

    @GetMapping("/status")
    public ResponseEntity<ResponseRoleStatusDTO> getRoleStatus(@AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getRoleStatus(authUser));
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseRoleStatusDTO> changeAdminStatus(@RequestBody @Valid final RequestRoleStatusDTO requestRoleStatusDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.changeAdminStatus(requestRoleStatusDTO));
    }
}
