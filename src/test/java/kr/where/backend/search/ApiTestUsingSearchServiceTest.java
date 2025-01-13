package kr.where.backend.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Image;
import kr.where.backend.api.json.Versions;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.group.entity.Group;
import kr.where.backend.oauthtoken.OAuthTokenService;
import kr.where.backend.search.dto.ResponseSearchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureMockMvc
public class ApiTestUsingSearchServiceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchService searchService;

    AuthUser authUser;
    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities
                = Stream.of("ROLE_USER")
                .map(SimpleGrantedAuthority::new)
                .toList();
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @Test
    @DisplayName("컨트롤러에서 searchUser 호출 검증")
    public void SearchApiTest() throws Exception {
        // given
        String requestParam = "sooh";
        ResponseSearchDTO searchDTO1 = new ResponseSearchDTO(11111, "soohlee", "", "상태메시지", "c1r1s1", true, true, true);
        ResponseSearchDTO searchDTO2 = new ResponseSearchDTO(22222, "soogjilee", "", "상태메시지", "c1r1s1", true, true, true);
        List<ResponseSearchDTO> mockResponse = List.of(searchDTO1, searchDTO2);

        // searchUser 모킹
        when(searchService.searchUser(Mockito.eq(requestParam), Mockito.any(AuthUser.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/v3/search/new")
                                .param("keyWord", requestParam)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(mockResponse))) // JSON 응답 확인
                .andDo(MockMvcResultHandlers.print());

        // searchUser 호출 여부 검증
        Mockito.verify(searchService, Mockito.times(1)).searchUser(Mockito.eq(requestParam), Mockito.any(AuthUser.class));
    }
}
