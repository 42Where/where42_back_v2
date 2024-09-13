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
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
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
        Version v = new Version("1.3.1", "ios");
        versionRepository.save(v);
    }

    @Test
    @DisplayName("request 버전 형식 유효성 검사")
    public void checkValidVersionFormat() {
        //given
        Version v = versionRepository.findByOsType("ios").orElseThrow(VersionException.InvalidVersionFormatException::new);

        //when
//        String test0 = v.checkValidVersionFormat("0.0.0");
//        String test1 = v.checkValidVersionFormat("2.1.7");

        //then
        assertThat(v.checkValidVersionFormat("0.0.0")).isEqualTo("0.0.0");
        assertThat(v.checkValidVersionFormat("2.1.7")).isEqualTo("2.1.7");

        assertThatThrownBy(() -> Version.checkValidVersionFormatForTest("000000.0.0"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
        assertThatThrownBy(() -> Version.checkValidVersionFormatForTest("-10.0"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
        assertThatThrownBy(() -> Version.checkValidVersionFormatForTest("432554253545325.1.9"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
        assertThatThrownBy(() -> Version.checkValidVersionFormatForTest("1...7.0"))
                .isInstanceOf(VersionException.InvalidVersionFormatException.class)
                .hasMessage(null);
    }

    @Test
    @DisplayName("버전 정규 표현식 유효성 검사 및 버전 체크에 관한 테스트")
    public void versionUpdate() {
        //given
        Version v = versionRepository.findByOsType("ios").orElseThrow(VersionException.NotAllowedOsException::new);

        //when
        boolean test1 = versionService.compareVersion(v, "1.3.1");
        boolean test2 = versionService.compareVersion(v, "1.4.1");

        //then
        assertThat(test1).isTrue();
        assertThat(test2).isFalse();
        assertThatThrownBy(() -> versionService.compareVersion(v, "1.2.1"))
                .isInstanceOf(VersionException.RequireVersionUpgradeException.class)
                .hasMessage(null);
    }
}
