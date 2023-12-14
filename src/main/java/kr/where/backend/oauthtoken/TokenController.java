package kr.where.backend.oauthtoken;

import java.util.List;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Cluster;
import kr.where.backend.api.json.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/token")
@RequiredArgsConstructor
public class TokenController {

    private final OauthTokenService oauthTokenService;
    private final TokenApiService tokenApiService;
    private final IntraApiService intraApiService;

    // test 용

    /**
     * REDIRECT_URI http://localhost:8080/v3/token
     * https://api.intra.42.fr/oauth/authorize?client_id= 로 시작하는 긴 redirect url 실행하면 이곳으로 code 반환
     */
    @GetMapping("")
    public String createAccessToken(@RequestParam("code") String code) {
        System.out.println(code);
        return code;
    }

    // 프론트가 없어서 위에서 받은 code 를 수동으로 넣어줘야 함
    @PostMapping("")
    public String createToken(@RequestParam("name") String name, @RequestParam("code") String code) {
        final OAuthToken oAuthToken = tokenApiService.getOAuthToken(code);
        oauthTokenService.createToken(name, oAuthToken);
        return oAuthToken.getAccess_token();
    }

    @PostMapping("/hane")
    public String createHaneToken() {
        OAuthToken oAuthToken = new OAuthToken();
        oauthTokenService.createToken("hane", oAuthToken);
        return "hane";
    }

    @PostMapping("/update/hane")
    public String updateHaneToken(@RequestParam("code") String token) {
        oauthTokenService.updateHaneToken(token);
        return token;
    }

    // api test

//    @GetMapping("/user")
//    public CadetPrivacy getUserInfo() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final CadetPrivacy cadetPrivacy = intraApiService.getCadetPrivacy(accessToken, "jonhan");
//
//        return cadetPrivacy;
//    }
//
//    @GetMapping("/image")
//    public List<CadetPrivacy> get42Image() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final List<CadetPrivacy> list = intraApiService.getCadetsImage(accessToken, 1);
//        return list;
//    }
//
//    @GetMapping("/info")
//    public List<Cluster> get42ClusterInfo() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final List<Cluster> list = intraApiService.getCadetsInCluster(accessToken, 1);
//        return list;
//    }
//
//    @GetMapping("/end")
//    public List<Cluster> get42LocationEnd() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final List<Cluster> list = intraApiService.getLogoutCadetsLocation(accessToken, 1);
//        return list;
//    }
//
//    @GetMapping("/begin")
//    public List<Cluster> get42LocationBegin() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final List<Cluster> list = intraApiService.getLoginCadetsLocation(accessToken, 1);
//        return list;
//    }
//
//    @GetMapping("/range/info")
//    public List<CadetPrivacy> get42UsersInfoInRange() {
//        final String accessToken = oauthTokenService.findAccessToken("test");
//        final List<CadetPrivacy> list = intraApiService.getCadetsInRange(accessToken, "jon", 1);
//
//        return list;
//    }
}
