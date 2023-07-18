package kr.where.backend.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @PostMapping("/admin/login")
    public ResponseEntity adminLogin(HttpSession session, @RequestBody AdminInfo admin){
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_LOGIN_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/admin/secret-admin")
    public ResponseEntity updateAdminServerSecret(HttpServletRequest req, @RequestBody Map<String, String> secret) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        adminRepository.insertAdminSecret(secret.get("secret"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SECRET_UPDATE_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/admin/secret-member")
    public ResponseEntity updateServerSecret(HttpServletRequest req, @RequestBody Map<String, String> secret) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        tokenRepository.insertSecret(secret.get("secret"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SECRET_UPDATE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/admin/auth/code")
    public String adminAuthLogin() {
        return "https://api.intra.42.fr/oauth/authorize?client_id=u-s4t2ud-b40ead3720ac3ae095283c426699403ad2949fd0c54785d38d6f9f360670ed2e&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fv2%2Fauth%2Fadmin%2Fcallback&response_type=codehttps://api.intra.42.fr/oauth/authorize?client_id=u-s4t2ud-b40ead3720ac3ae095283c426699403ad2949fd0c54785d38d6f9f360670ed2e&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fv2%2Fauth%2Fadmin%2Fcallback&response_type=code";
    }

    @PostMapping("/admin/auth/token")
    public ResponseEntity insertAdminToken(HttpServletRequest req, @RequestBody Map<String, String> code) {
        log.info("[insertAdminToken] Admin Token을 주입합니다.");
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        OAuthToken oAuthToken = adminApiService.getAdminOAuthToken(adminRepository.callAdminSecret(), code.get("code"));
        adminRepository.saveAdmin("admin", oAuthToken);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_TOKEN_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/admin/hane/token")
    public ResponseEntity insertHane(HttpServletRequest req, @RequestBody Map<String, String> token) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        adminRepository.insertHane(token.get("token"));
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.HANE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/admin/incluster")
    public ResponseEntity findAllInClusterCadet(HttpServletRequest req) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        backgroundService.updateAllInClusterCadet();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.IN_CLUSTER), HttpStatus.OK);
    }

    @DeleteMapping("/admin/flash")
    public ResponseEntity resetFlash(HttpServletRequest req) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        flashDataRepository.resetFlash();
        log.info("[reset-flash] flash data 초기화를 완료하였습니다.");
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.RESET_FLASH), HttpStatus.OK);
    }

    @PostMapping("/admin/image")
    public ResponseEntity getAllCadetImages(HttpServletRequest req) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        backgroundService.getAllCadetImages();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.GET_IMAGE_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/admin/createdAt")
    public ResponseEntity getAllCadetCreateAt(HttpServletRequest req) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        adminService.getSignUpDate();
        return null;
    }

    @DeleteMapping("/admin/member")
    public ResponseEntity deleteMember(HttpServletRequest req, @RequestParam(name = "name") String name) {
        if (!adminService.findAdminBySession(req))
            throw new SessionExpiredException();
        adminService.deleteMember(name);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_MEMBER), HttpStatus.OK);
    }

    @GetMapping("/admin/logout")
    public ResponseEntity adminLogout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null)
            session.invalidate();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.ADMIN_LOGOUT_SUCCESS), HttpStatus.OK);
    }
}
