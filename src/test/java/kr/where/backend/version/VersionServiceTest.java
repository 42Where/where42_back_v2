package kr.where.backend.version;

import java.util.Collection;
import java.util.List;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.version.exception.VersionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class VersionServiceTest {

    @Autowired
    VersionService versionService;
    @Autowired
    VersionRepository versionRepository;

    private AuthUser authUser;
    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        Version v = new Version("1.3.1", "IOS");
        versionRepository.save(v);
    }

    @Test
    @DisplayName("request 버전 형식 유효성 검사")
    public void checkValidVersionFormat() {
        //given
        Version v = versionRepository.findByOsType("IOS")
                .orElseThrow(VersionException.InvalidVersionFormatException::new);

        //then
        assertThat(v.checkValidVersionFormat("0.0.0")).isEqualTo("0.0.0");
        assertThat(v.checkValidVersionFormat("2.1.7")).isEqualTo("2.1.7");

        assertThatThrownBy(() -> v.checkValidVersionFormat("-10.0"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
        assertThatThrownBy(() -> v.checkValidVersionFormat("1...7.0"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
    }

    @Test
    @DisplayName("버전 레벨 비교 및 업데이트 테스트")
    public void versionUpdate() {
        //given
        Version v = versionRepository.findByOsType("IOS").orElseThrow(VersionException.NotAllowedOsException::new);

        //when
        boolean test1 = versionService.compareVersion(v, "1.3.1");
        boolean test2 = versionService.compareVersion(v, "1.4.6");

        //then
        assertThat(test1).isTrue();
        assertThat(test2).isFalse();
        assertThatThrownBy(() -> versionService.compareVersion(v, "1.2.1"))
                .isInstanceOf(VersionException.RequireVersionUpgradeException.class)
                .hasMessage(null);
    }

    @Test
    @DisplayName("허용 OS 테스트")
    public void allowOsTest() {
        // 예외가 발생하지 않는 경우 (문자열이 존재하는 경우)
        OsType.checkAllowedOs("IOS");
        OsType.checkAllowedOs("ANDROID");

        // 예외가 발생하는 경우 (없는 문자열)
        assertThrows(VersionException.NotAllowedOsException.class, () -> {
            OsType.checkAllowedOs("WINDOWS"); // 없는 OS
        });

        assertThatThrownBy(() -> OsType.checkAllowedOs("WINDOWS"))
                .isInstanceOf(VersionException.NotAllowedOsException.class);
    }

}
