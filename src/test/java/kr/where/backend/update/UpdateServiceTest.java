//package kr.where.backend.update;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import kr.where.backend.api.json.ClusterInfo;
//import kr.where.backend.api.json.Image;
//import kr.where.backend.api.json.User;
//import kr.where.backend.api.json.Versions;
//import kr.where.backend.auth.authUser.AuthUser;
//import kr.where.backend.member.Member;
//import kr.where.backend.member.MemberRepository;
//import kr.where.backend.oauthtoken.OAuthTokenService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test")
//@Rollback
//public class UpdateServiceTest {
//
//    @MockBean
//    private OAuthTokenService oAuthTokenService;
//    @Autowired
//    private UpdateService updateService;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    AuthUser authUser;
//    @BeforeEach
//    public void setUp() {
//        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
//        authUser = new AuthUser(135436, "noneExistMember", 1L);
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
//    }
//
//    @Test
//    @DisplayName("자리 스케쥴링시 없는 업데이트 해야할 멤버가 DB에 없으면 비동의 멤버로 생성하는 테스트")
//    void createDisagreeMemberIfNotExistsDuringScheduling() {
//        // given
//        List<ClusterInfo> noneExistMember = new ArrayList<>();
//        // Mocking ClusterInfo
//        ClusterInfo clusterInfoMock = mock(ClusterInfo.class);
//        when(clusterInfoMock.getId()).thenReturn(1);
//        when(clusterInfoMock.getBegin_at()).thenReturn("2025-01-01T10:00:00Z");
//        when(clusterInfoMock.getEnd_at()).thenReturn("2025-01-01T12:00:00Z");
//        // Mocking User
//        User userMock = mock(User.class);
//        when(userMock.getId()).thenReturn(44444);
//        when(userMock.getLogin()).thenReturn("noneExist");
//        when(userMock.getLocation()).thenReturn("c4r4s4");
//        when(userMock.isActive()).thenReturn(true);
//        when(userMock.getCreated_at()).thenReturn("2020-01-01T12:00:00Z");
//        // Mocking Image and Versions
//        Image imageMock = mock(Image.class);
//        Versions versionsMock = mock(Versions.class);
//        when(versionsMock.getSmall()).thenReturn("img uri");
//        when(imageMock.getVersions()).thenReturn(versionsMock);
//        when(userMock.getImage()).thenReturn(imageMock);
//        // User mock을 ClusterInfo mock에 세팅
//        when(clusterInfoMock.getUser()).thenReturn(userMock);
//        noneExistMember.add(clusterInfoMock);
//        // 가짜 하네토큰발급 하게끔 함.
//        when(oAuthTokenService.findAccessToken("Hane")).thenReturn("testToken");
//
//        //when
//        updateService.updateLocation(noneExistMember);
//
//        //then
//        Member nonExist = memberRepository.findByIntraName("noneExist").get();
//        assertThat(nonExist.getIntraName()).isEqualTo("noneExist");
//        assertThat(nonExist.isAgree()).isFalse();
//    }
//}
