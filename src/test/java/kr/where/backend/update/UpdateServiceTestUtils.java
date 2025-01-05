package kr.where.backend.update;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.ClusterInfo;
import kr.where.backend.api.json.Image;
import kr.where.backend.api.json.OAuthTokenDto;
import kr.where.backend.api.json.User;
import kr.where.backend.api.json.Versions;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.MemberService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class UpdateServiceTestUtils {

    public static void memberCreateAndSave(MemberService memberService, AuthUser authUser, String location, String haneInOut) {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(authUser.getIntraId(), authUser.getIntraName(), location, "image", true, "2022-10-31", 29);
        Hane hane = Hane.create(haneInOut);
        memberService.createAgreeMember(cadetPrivacy, hane);
    }

    public static ClusterInfo createClusterInfoWithReflection(
            Integer clusterId,
            String beginAt,
            String endAt,
            Integer userId,
            String intraId,
            String location,
            boolean userActive,
            String userCreatedAt,
            String smallImageVersion
    ) {
        try {
            // 1. ClusterInfo 객체 생성
            ClusterInfo clusterInfo = ClusterInfo.class.getDeclaredConstructor().newInstance();

            // 2. User 객체 생성
            User user = createUserWithReflection(userId, intraId, location, userActive, userCreatedAt, smallImageVersion);

            // 3. ClusterInfo 필드 값 설정
            setField(clusterInfo, "id", clusterId);
            setField(clusterInfo, "begin_at", beginAt);
            setField(clusterInfo, "end_at", endAt);
            setField(clusterInfo, "user", user);

            return clusterInfo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ClusterInfo object via reflection", e);
        }
    }

    private static User createUserWithReflection(
            Integer id,
            String intraId,
            String location,
            boolean active,
            String createdAt,
            String smallImageVersion
    ) throws Exception {
        // 1. User 객체 생성
        User user = User.class.getDeclaredConstructor().newInstance();

        // 2. Versions 객체 생성
        Versions versions = Versions.create(smallImageVersion);

        // 3. Image 객체 생성
        Image image = Image.create(versions);

        // 4. User 필드 값 설정
        setField(user, "id", id);
        setField(user, "login", intraId);
        setField(user, "image", image);
        setField(user, "location", location);
        setField(user, "active", active);
        setField(user, "created_at", createdAt);

        return user;
    }

    public static OAuthTokenDto createOAuthTokenDtoWithReflection(
            String accessToken,
            String tokenType,
            int expiresIn,
            String refreshToken,
            int createdAt
    ) {
        try {
            // 1. OAuthTokenDto 객체 생성
            OAuthTokenDto oAuthTokenDto = OAuthTokenDto.class.getDeclaredConstructor().newInstance();

            // 2. OAuthTokenDto 필드 값 설정
            setField(oAuthTokenDto, "access_token", accessToken);
            setField(oAuthTokenDto, "token_type", tokenType);
            setField(oAuthTokenDto, "expires_in", expiresIn);
            setField(oAuthTokenDto, "refresh_token", refreshToken);
            setField(oAuthTokenDto, "created_at", createdAt);

            return oAuthTokenDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OAuthTokenDto object via reflection", e);
        }
    }
}
