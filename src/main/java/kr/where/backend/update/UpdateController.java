package kr.where.backend.update;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.update.swagger.UpdateApiDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v3/update")
public class UpdateController implements UpdateApiDocs {

    private final UpdateService updateService;

    @PostMapping("")
    public ResponseEntity<String> update(final AuthUser authUser) {
        updateService.updateMemberLocations();

        return ResponseEntity.ok("update complete");
    }
}
