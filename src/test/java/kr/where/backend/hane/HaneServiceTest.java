package kr.where.backend.hane;

import java.util.Collection;
import java.util.List;
import kr.where.backend.api.HaneApiService;
import kr.where.backend.api.json.hane.HaneRequestDto;
import kr.where.backend.api.json.hane.HaneResponseDto;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException.NoMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class HaneServiceTest {
    @Autowired
    HaneApiService haneApiService;
    @Autowired
    MemberRepository memberRepository;
    private AuthUser authUser;

    @BeforeEach
    public void setUp() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(135436, "suhwpark", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
    }

    @Test
    public void useHaneNewApi() {
        //given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHRmdW5jIjoiV2hlcmU0MiIsImlhdCI6MTcxOTk4MzIzOSwiZXhwIjoxNzYwNTg4MDM5fQ.K5GZNdwmSLgQSn-Lp5O19b9S-cZvlBsTI8_6uiEwsAQ";

        //when
        List<HaneRequestDto> haneRequestDtoList = memberRepository.findAllToUseHaneApi()
                .orElseThrow(NoMemberException::new);
        List<HaneResponseDto> haneResponseDtoList = haneApiService.getHaneInfoOfAll(haneRequestDtoList, token);
        //then
        haneResponseDtoList.forEach(h -> System.out.println(h.toString()));

    }
}
