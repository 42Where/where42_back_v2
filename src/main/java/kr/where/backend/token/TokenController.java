package kr.where.backend.token;

import java.util.List;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.mappingDto.Cluster;
import kr.where.backend.api.mappingDto.OAuthToken;
import kr.where.backend.member.DTO.Seoul42;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final TokenApiService tokenApiService;
    private final IntraApiService intraApiService;

    // test 용

    /**
     * api/http/HttpHeader 에 test 용 CLIENT_ID, SECRET, REDIRECT_URI(http://localhost:8080/v3/token) 로 변경후
     * https://api.intra.42.fr/oauth/authorize?client_id= 로 시작하는 긴 redirect url 입력하면 이곳으로 넘어와서 토큰 생성
     */
    @GetMapping("")
    public String createToken(@RequestParam("code") String code) {
        final OAuthToken oAuthToken = tokenApiService.getOAuthToken(code);
        tokenService.createToken("test", oAuthToken);
        return oAuthToken.getAccess_token();
    }

    @GetMapping("/me")
    public Seoul42 getMeInfo() {
        final String accessToken = tokenService.findAccessToken("test");
        final Seoul42 seoul42 = intraApiService.getMeInfo(accessToken);
        return seoul42;
    }

    @GetMapping("/user")
    public Seoul42 getUserInfo() {
        final String accessToken = tokenService.findAccessToken("test");
        final Seoul42 seoul42 = intraApiService.getUserInfo(accessToken, "jonhan");
        return seoul42;
    }

    @GetMapping("/image")
    public List<Seoul42> get42Image() {
        final String accessToken = tokenService.findAccessToken("test");
        final List<Seoul42> list = intraApiService.get42Image(accessToken, 1);
        return list;
    }

    @GetMapping("/info")
    public List<Cluster> get42ClusterInfo() {
        final String accessToken = tokenService.findAccessToken("test");
        final List<Cluster> list = intraApiService.get42ClusterInfo(accessToken, 1);
        return list;
    }

    @GetMapping("/end")
    public List<Cluster> get42LocationEnd() {
        final String accessToken = tokenService.findAccessToken("test");
        final List<Cluster> list = intraApiService.get42LocationEnd(accessToken, 1);
        return list;
    }

    @GetMapping("/begin")
    public List<Cluster> get42LocationBegin() {
        final String accessToken = tokenService.findAccessToken("test");
        final List<Cluster> list = intraApiService.get42LocationBegin(accessToken, 1);
        return list;
    }

    @GetMapping("/range/info")
    public List<Seoul42> get42UsersInfoInRange() {
        final String accessToken = tokenService.findAccessToken("test");
        final List<Seoul42> list = intraApiService.get42UsersInfoInRange(accessToken, "jon");
        return list;
    }
}
