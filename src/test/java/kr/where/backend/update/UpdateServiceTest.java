package kr.where.backend.update;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.*;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.api.json.hane.HaneResponseDto;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Rollback
@Slf4j
public class UpdateServiceTest {

    @MockBean
    private OAuthTokenService oAuthTokenService;
    @Autowired
    private UpdateService updateService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private HaneApiService haneApiService;

    AuthUser authUser;

    private static final Integer CAMPUS_ID = 29;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "noneExistMember", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));

        save500Member();
    }
    private void save500Member() {
        for (int i = 1 ; i <= 800; i++) {
            memberCreateAndSave(i, "member" + i, "location", "IN", i);
        }
    }

    private List<HaneResponseDto> makeHaneResponse(String inOrOutStatus) {
        List<HaneResponseDto> dtos = new ArrayList<>();
        for(int i = 1; i <= 500; i++) {
            HaneResponseDto dto = new HaneResponseDto();
            ReflectionTestUtils.setField(dto, "login", "member" + i);
            ReflectionTestUtils.setField(dto, "inoutState", inOrOutStatus);
            dtos.add(dto);
        }
        return dtos;
    }

    @Test
    @DisplayName("updateInCluster 스케줄러 속도 비교 Test 개선")
    public void updateInClusterTest() {
        List<HaneResponseDto> inResponse = makeHaneResponse("IN");
        long startTime = System.nanoTime();  // 시작 시간 측정
        memberService.updateUpdatableMember(inResponse);
        long endTime = System.nanoTime();    // 종료 시간 측정
        long duration = (endTime - startTime) / 1_000_000; // 밀리초(ms) 단위 변환

        List<HaneResponseDto> outResponse = makeHaneResponse("OUT");
        long beforeStartTime = System.nanoTime();  // 시작 시간 측정
        beforeUpdateInClusterTest(outResponse);
        long beforeEndTime = System.nanoTime();    // 종료 시간 측정
        long beforeCodeDuration = (beforeEndTime - beforeStartTime) / 1_000_000; // 밀리초(ms) 단위 변환

        System.out.println("updateInCluster 개선 후 실행 시간: " + duration + " ms");
        System.out.println("updateInCluster 개선 전 실행 시간: " + beforeCodeDuration + " ms");
    }

    private void beforeUpdateInClusterTest(List<HaneResponseDto> haneResponse) {
        haneResponse.stream()
                .filter(response -> response.getInoutState() != null)
                .forEach(response -> {
                    haneApiService.updateMemberInOrOutState(
                            memberService.findByIntraName(response.getLogin())
                                    .orElseThrow(MemberException.NoMemberException::new),
                            response.getInoutState());
                });
    }

    private void memberCreateAndSave(int intraId, String intraName, String location, String haneInOut, long defaultGroupId) {
        AuthUser authUser = new AuthUser(intraId, intraName, defaultGroupId);
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(intraId, intraName, location, "image", true, "2022-10-31", CAMPUS_ID);
        Hane hane = Hane.create(haneInOut);
        memberService.createAgreeMember(cadetPrivacy, hane);
    }

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
}
