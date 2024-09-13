package kr.where.backend.version;

import kr.where.backend.member.dto.ResponseMemberDTO;
import kr.where.backend.version.dto.CheckVersionDTO;
import kr.where.backend.version.dto.ResponseVersionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VersionService {

    private final VersionRepository versionRepository;

    @Transactional
    public void update(Version version) {
        versionRepository.save(version);
    }

    public ResponseVersionDTO checkVersion(CheckVersionDTO checkVersionDTO) {

        // 보안측면(request가 예상 외 값이 들어왔을 때)에서 봤을 땐 없는 os가 들어오면 예외를 던지는게 아니라 enum[IOS, ANDROID]으로 체크해서 정해진 os만 유효하게끔.
        final Version version = versionRepository.findByOsType(checkVersionDTO.getOs())
                        .orElse(new Version(checkVersionDTO.getVersion(), checkVersionDTO.getOs()));
        // 커스텀익셉션 나중에 작성할 것임.
        //유효성 체크
        version.

        version.setLatestVersion(checkVersionDTO.getVersion());

        return ResponseMemberDTO.builder().member(member).build();

    }

    //유효성 체크


    //
}
