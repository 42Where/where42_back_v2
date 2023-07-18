package kr.where.backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth/login")
    public ResponseEntity login(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        token = tokenService.findAccessToken(res, key);
        Seoul42 seoul42 = apiService.getMeInfo(token);
        Member member = memberRepository.findByName(seoul42.getLogin());
        if (member == null)
            throw new UnregisteredMemberException(seoul42);
        session = req.getSession();
        session.setAttribute("id", member.getId());
        session.setMaxInactiveInterval(60 * 60);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/auth/permission")
    public ResponseEntity checkAgree(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        token = tokenService.findAccessToken(res, key);
        session = req.getSession(false);
        if (session != null)
            throw new CannotAccessAgreeException();
        Seoul42 seoul42 = apiService.getMeInfo(token);
        Member member = memberRepository.findByName(seoul42.getLogin());
        if (member != null) {
            session = req.getSession();
            session.setAttribute("id", member.getId());
            throw new CannotAccessAgreeException();
        }
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.UNREGISTERED), HttpStatus.OK);
    }

    @PostMapping("/auth/token")
    public ResponseEntity makeToken(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> code) {
        Seoul42 seoul42 = tokenService.beginningIssue(res, code.get("code"));
        session = req.getSession(false);
        if (session != null) {
            if (memberService.findBySession(req) != null)
                return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
        }
        Member member = memberRepository.findByName(seoul42.getLogin());
        if (member == null)
            throw new UnregisteredMemberException(seoul42);
        session = req.getSession();
        session.setAttribute("id", member.getId());
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/auth/logout")
    public ResponseEntity logout(HttpServletRequest req, HttpServletResponse res) {
        res.addCookie(oven.burnCookie("ID"));
        res.addCookie(oven.bakingMaxAge("0", 0));
        session = req.getSession(false);
        if (session != null)
            session.invalidate();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGOUT_SUCCESS), HttpStatus.OK);
    }
}
