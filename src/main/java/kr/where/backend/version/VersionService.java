package kr.where.backend.version;

import java.util.Objects;
import kr.where.backend.version.dto.CheckVersionDTO;
import kr.where.backend.version.dto.ResponseVersionDTO;
import kr.where.backend.version.exception.VersionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VersionService {

    private final VersionRepository versionRepository;

    @Transactional
    public ResponseVersionDTO checkVersion(final CheckVersionDTO checkVersionDTO) {
        OsType.checkAllowedOs(checkVersionDTO.getOs());

        // 보안측면(request가 예상 외 값이 들어왔을 때)에서 봤을 땐 없는 os가 들어오면 예외를 던지는게 아니라 enum[IOS, ANDROID]으로 체크해서 정해진 os만 유효하게끔.
        final Version version = versionRepository.findByOsType(checkVersionDTO.getOs()
                        .toUpperCase())
                        .orElseThrow(VersionException.NotAllowedOsException::new);

        version.checkValidVersionFormat(checkVersionDTO.getVersion());

        //버전 값 체크

        if (!compareVersion(version, checkVersionDTO.getVersion())) {
            version.updateVersion(checkVersionDTO.getVersion());
        }

        return ResponseVersionDTO
                .builder()
                .latestVersion(
                        version.getLatestVersion()
                )
                .build();

    }

    public boolean compareVersion(final Version version, final String request) {
        if (Objects.equals(version.getLatestVersion(), request)) {
            return true;
        }

        final String[] split = request.split("\\.");
        final String[] origin = version.getLatestVersion().split("\\.");

        for (int i = 0; i < 3; ++i) {
            int curr = Integer.parseInt(origin[i]);
            int requestV = Integer.parseInt(split[i]);

            if (curr > requestV) {
                throw new VersionException.RequireVersionUpgradeException();
            }
        }
        return false;
    }

    @Transactional
    public void init() {
        for (OsType os : OsType.values()) {
            versionRepository.findByOsType(os.name()).ifPresentOrElse(
                    version -> version.updateVersion("0.0.0"),
                    () -> saveDefaultVersion(os.name())
            );
        }
    }

    private void saveDefaultVersion(String type) {
        Version version = new Version("0.0.0", type);
        versionRepository.save(version);
    }
}
