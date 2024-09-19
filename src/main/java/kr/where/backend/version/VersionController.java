package kr.where.backend.version;

import kr.where.backend.version.dto.RequestVersionDTO;
import kr.where.backend.version.dto.ResponseVersionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/version")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @PostMapping("")
    public ResponseEntity<ResponseVersionDTO> checkVersion(@RequestBody final RequestVersionDTO requestVersionDTO) {

        return ResponseEntity.ok(
                versionService.checkVersion(requestVersionDTO)
        );
    }

    /**
     * 서버에 저장된 Enum[IOS, ANDROID]을 기준으로 DB에 저장.
     * 버전은 0.0.0 강제 초기화
     *  - 추후에 각 os에서 checkVersion 호출 시 업데이트 가능함.
     * @return ResponseEntity.status(HttpStatus.OK).build()
     */
    @GetMapping("/init")
    public ResponseEntity<String> initVersion() {
        versionService.init();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
