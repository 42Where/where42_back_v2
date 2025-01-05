package kr.where.backend.search;


import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.oauthtoken.exception.OAuthTokenException;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.exception.SearchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
public class SearchServiceTest {

    @Autowired
    SearchService searchService;
    @Autowired
    MemberService memberService;

    AuthUser authUser;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @DisplayName("유효하지 않은 keyword값이 들어왔을 경우 예외처리")
    @ValueSource(strings = {"수환", "a", "", "%%"})
    @ParameterizedTest
    public void invalidKeyWord(final String keyWord) {
        //then
        assertThatThrownBy(() -> searchService.search(keyWord, authUser))
                .isInstanceOf(SearchException.class);

        assertThatThrownBy(() -> searchService.search(keyWord, authUser))
                .isInstanceOf(SearchException.class);
    }

    @DisplayName("토큰이 없을 시 실패하는 test")
    @Test
    public void getCadetPrivacy() {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(135436, "suhwpark", "c1r1s1", "image", true, "2022-10-31", 29);
        Hane hane = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);

        //then
        assertThatThrownBy(() -> searchService.search("jn", authUser))
                .isInstanceOf(OAuthTokenException.InvalidOAuthTokenException.class);
    }
}