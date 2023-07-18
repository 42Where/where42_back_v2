package kr.where.backend.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/home")
    public ResponseEntity home(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        token = tokenService.findAccessToken(res, key);
        memberService.findBySessionWithToken(req, token);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }
}
