package kr.where.backend.oauthtoken;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.TokenApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Cluster;
import kr.where.backend.api.json.OAuthTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v3/token")
@RequiredArgsConstructor
@Tag(name = "oauth", description = "oauth token API")
public class OAuthTokenController {

    private final OAuthTokenService oauthTokenService;
    private final TokenApiService tokenApiService;
    private final IntraApiService intraApiService;

    /**
     * OAuth 인증 후 해당 uri 로 code 반환됨
     */
    @GetMapping("")
    public void createAccessToken(@RequestParam("code") String code) {
        log.info("code : {}", code);
    }

    /**
     * code로 OAuth token 받아와서 생성 & 업데이트
     * @param name : 생성할 token 이름
     * @param code : log에 찍힌 code
     */
    @PostMapping("")
    public ResponseEntity createToken(@RequestParam("name") String name, @RequestParam("code") String code) {
        final OAuthTokenDto oAuthToken = tokenApiService.getOAuthToken(code);
        oauthTokenService.createToken(name, oAuthToken);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * hane token 생성 & 업데이트
     * @param token : hane token
     */
    @PostMapping("/update/hane")
    public ResponseEntity updateHaneToken(@RequestParam("code") final String token) {
        oauthTokenService.updateHaneToken(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * @param name : 삭제할 token 이름
     */
    @DeleteMapping("")
    public void deleteToken(@RequestParam("name") String name) {
        oauthTokenService.deleteToken(name);
    }




    /**
     * api test
     * OAuthToken 넣고 모든 api 호출 성공하면 삭제
     */
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
