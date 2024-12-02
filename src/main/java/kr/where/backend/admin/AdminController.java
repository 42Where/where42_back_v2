package kr.where.backend.admin;

import jakarta.validation.Valid;
import kr.where.backend.admin.dto.RequestAdminStatusDTO;
import kr.where.backend.admin.dto.ResponseAdminStatusDTO;
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
    public ResponseEntity<ResponseAdminStatusDTO> getAdminStatus(@AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdminStatus(authUser));
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseAdminStatusDTO> changeAdminStatus(@RequestBody @Valid final RequestAdminStatusDTO requestAdminStatusDTO,
                                                                    @AuthUserInfo final AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.changeAdminStatus(requestAdminStatusDTO, authUser));
    }
}
