package kr.where.backend.version;

import kr.where.backend.version.dto.CheckVersionDTO;
import kr.where.backend.version.dto.ResponseVersionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/version")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @PostMapping("")
    public ResponseEntity<ResponseVersionDTO> checkVersion(@RequestBody final CheckVersionDTO checkVersionDTO) {

        return ResponseEntity.ok(
                versionService.checkVersion(checkVersionDTO)
        );
    }
}
