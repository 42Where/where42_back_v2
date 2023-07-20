package kr.where.backend.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.where.backend.admin.dto.AdminInfo;
import kr.where.backend.admin.dto.KeyValueInfo;
import kr.where.backend.exception.customException.AdminLoginFailException;
import kr.where.backend.exception.customException.SessionExpiredException;
import kr.where.backend.utils.response.Response;
import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.ResponseWithData;
import kr.where.backend.utils.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v3/admin")
@Tag(name = "admin", description = "admin API")
public class AdminController {

    @Operation(
            summary = "admin login API",
            description = "관리자 로그인 및 세션 생성",
            parameters = {
                    @Parameter(name = "session", description = "관리자 세션 생성용 세션", required = false),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "관리자 로그인용 Id 및 PWD", required = true, content = @Content(schema = @Schema(implementation = AdminInfo.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"관리자 로그인 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "일치하는 관리자 없음", content = @Content(schema = @Schema(implementation = AdminLoginFailException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"관리자 로그인 실패\"}")})),
            })
    @PostMapping("/login")
    public ResponseEntity adminLogin(HttpSession session, @RequestBody AdminInfo admin){
//        adminService.adminLogin(admin.getName(), admin.getPasswd());
//        session.setAttribute("name", admin.getName());
//        session.setMaxInactiveInterval(30 * 60);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_LOGIN_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "update secret_id for admin API",
            description = "관리자용 42api secret_id DB 갱신",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "갱신할 secret id", required = true,
                    content = @Content(schema = @Schema(implementation = KeyValueInfo.class),
                                        examples = @ExampleObject(value = "{\"secret\":\"secret id\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"관리자 로그인 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/secret-admin")
    public ResponseEntity updateAdminServerSecret(HttpServletRequest req, @RequestBody Map<String, String> secret) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        adminRepository.insertAdminSecret(secret.get("secret"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SECRET_UPDATE_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "update secret_id for user API",
            description = "사용자용 42api secret_id DB 갱신",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "갱신할 secret id", required = true,
                    content = @Content(schema = @Schema(implementation = KeyValueInfo.class),
                            examples = @ExampleObject(value = "{\"secret\":\"secret id\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"시크릿 아이디 갱신 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/secret-member")
    public ResponseEntity updateServerSecret(HttpServletRequest req, @RequestBody Map<String, String> secret) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        tokenRepository.insertSecret(secret.get("secret"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SECRET_UPDATE_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "get 42api code API",
            description = "관리자 42api application code 획득용 주소",
            responses = {
                    @ApiResponse(responseCode = "200", description = "code 획득", content = @Content(schema = @Schema(type = "string"))),
            })
    @GetMapping("/auth/code")
    public String adminAuthLogin() {
        return "https://api.intra.42.fr/oauth/authorize?client_id=u-s4t2ud-b40ead3720ac3ae095283c426699403ad2949fd0c54785d38d6f9f360670ed2e&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fv2%2Fauth%2Fadmin%2Fcallback&response_type=codehttps://api.intra.42.fr/oauth/authorize?client_id=u-s4t2ud-b40ead3720ac3ae095283c426699403ad2949fd0c54785d38d6f9f360670ed2e&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fv2%2Fauth%2Fadmin%2Fcallback&response_type=code";
    }

    @Operation(
            summary = "insert admin token API",
            description = "관리자 access token DB 저장",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "42api 요청용 code", required = true,
                    content = @Content(schema = @Schema(implementation = KeyValueInfo.class),
                            examples = @ExampleObject(value = "{\"code\":\"42api 요청용 code\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"관리자 토큰 갱신 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/auth/token")
    public ResponseEntity insertAdminToken(HttpServletRequest req, @RequestBody Map<String, String> code) {
//        log.info("[insertAdminToken] Admin Token을 주입합니다.");
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        OAuthToken oAuthToken = adminApiService.getAdminOAuthToken(adminRepository.callAdminSecret(), code.get("code"));
//        adminRepository.saveAdmin("admin", oAuthToken);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_TOKEN_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "insert 24hane token API",
            description = "24hane access token DB 저장. 토큰 만료 시 24hane 담당자(현재 joopark)에게 연락하여 갱신",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "갱신할 토큰", required = true,
                    content = @Content(schema = @Schema(implementation = KeyValueInfo.class),
                            examples = @ExampleObject(value = "{\"token\":\"갱신할 토큰\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"하네 토큰 갱신 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/hane/token")
    public ResponseEntity insertHane(HttpServletRequest req, @RequestBody Map<String, String> token) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        adminRepository.insertHane(token.get("token"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.HANE_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "update all cadet in cluster API",
            description = "클러스터 아이맥에 로그인 해 있는 모든 카뎃들의 정보 갱신",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정보 저장 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"클러스터 아이맥 로그인 카뎃 갱신 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @GetMapping("/incluster")
    public ResponseEntity findAllInClusterCadet(HttpServletRequest req) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        backgroundService.updateAllInClusterCadet();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.IN_CLUSTER), HttpStatus.OK);
    }

    @Operation(
            summary = "reset flash data API",
            description = "모든 플래시 데이터 초기화",
            responses = {
                    @ApiResponse(responseCode = "200", description = "초기화 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"플래시 디비 초기화 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @DeleteMapping("/flash")
    public ResponseEntity resetFlash(HttpServletRequest req) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        flashDataRepository.resetFlash();
//        log.info("[reset-flash] flash data 초기화를 완료하였습니다.");
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.RESET_FLASH), HttpStatus.OK);
    }

    @Operation(
            summary = "reset all cadet's image API",
            description = "블랙홀 인원 제외 모든 카뎃들의 이미지 url 갱신 (새로운 기수 들어올 시 필수)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이미지 갱신 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"이미지 저장 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/image")
    public ResponseEntity getAllCadetImages(HttpServletRequest req) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        backgroundService.getAllCadetImages();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.GET_IMAGE_SUCCESS), HttpStatus.OK);
    }

    @Operation(
            summary = "insert all cadet's piscine start date API",
            description = "모든 카뎃들의 피신 시작일 삽입",
            responses = {
                    @ApiResponse(responseCode = "200", description = "피신 시작일 삽입 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"피신 시작일 삽입 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @PostMapping("/createdAt")
    public ResponseEntity getAllCadetCreateAt(HttpServletRequest req) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        adminService.getSignUpDate();
        return null;
    }

    @Operation(
            summary = "delete member API",
            description = "멤버 삭제시 사용하며 그룹 친구 정보 등 모두 삭제",
            parameters = {
                    @Parameter(name = "name", description = "삭제할 멤버 이름", required = true),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"멤버 삭제 성공\"}")})),
                    @ApiResponse(responseCode = "401", description = "세션이 만료된 경우 발생", content = @Content(schema = @Schema(implementation = SessionExpiredException.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 401, \"responseMsg\": \"세션 없음\"}")})),
            })
    @DeleteMapping("/member")
    public ResponseEntity deleteMember(HttpServletRequest req, @RequestParam(name = "name") String name) {
//        if (!adminService.findAdminBySession(req))
//            throw new SessionExpiredException();
//        adminService.deleteMember(name);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_MEMBER), HttpStatus.OK);
    }

    @Operation(
            summary = "admin logout API",
            description = "관리자 로그아웃(세션 삭제)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(value = "{\"statusCode\": 200, \"responseMsg\": \"관리자 로그아웃 성공\"}")})),
            })
    @GetMapping("/logout")
    public ResponseEntity adminLogout(HttpServletRequest req) {
//        HttpSession session = req.getSession(false);
//        if (session != null)
//            session.invalidate();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_LOGOUT_SUCCESS), HttpStatus.OK);
    }
}
