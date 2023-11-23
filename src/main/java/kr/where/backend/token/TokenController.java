package kr.where.backend.token;

import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.mappingDto.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final TokenApiService tokenApiService;

    // test 용
    @PostMapping("")
    public String createToken(@RequestParam String code) {
        OAuthToken oAuthToken = tokenApiService.getOAuthToken(code);
        tokenService.createToken("test", oAuthToken);
        return oAuthToken.getAccess_token();
    }
}
