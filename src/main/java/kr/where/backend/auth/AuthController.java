package kr.where.backend.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.utils.response.Response;
import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.ResponseWithData;
import kr.where.backend.utils.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Auth", description = "")
@RequestMapping("/v3/auth")
public class AuthController {

    @Operation(summary = "login API", description = "비로그인, 세션없는 유저 로그인 확인",
            parameters = {
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "등록되어 있는 멤버", content=@Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "401", description = "등록되지 않은 멤버", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/login")
    public ResponseEntity login(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
//        token = tokenService.findAccessToken(res, key);
//        Seoul42 seoul42 = apiService.getMeInfo(token);
//        Member member = memberRepository.findByName(seoul42.getLogin());
//        if (member == null)
//            throw new UnregisteredMemberException(seoul42);
//        session = req.getSession();
//        session.setAttribute("id", member.getId());
//        session.setMaxInactiveInterval(60 * 60);
//        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }

    @Operation(summary = "permission API", description = "동의 페이지 접근 가능 여부",
            parameters = {
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "동의 페이지 접근 가능", content=@Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "409", description = "이미 등록된 멤버", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/permission")
    public ResponseEntity checkAgree(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
//        token = tokenService.findAccessToken(res, key);
//        session = req.getSession(false);
//        if (session != null)
//            throw new CannotAccessAgreeException();
//        Seoul42 seoul42 = apiService.getMeInfo(token);
//        Member member = memberRepository.findByName(seoul42.getLogin());
//        if (member != null) {
//            session = req.getSession();
//            session.setAttribute("id", member.getId());
//            throw new CannotAccessAgreeException();
//        }
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.UNREGISTERED), HttpStatus.OK);
    }

    @Operation(summary = "makeToken API", description = "42api Access Token발급",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 가능", content=@Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "401", description = "등록 되지않은 멤버", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/token")
    public ResponseEntity makeToken(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> code) {
//        Seoul42 seoul42 = tokenService.beginningIssue(res, code.get("code"));
//        session = req.getSession(false);
//        if (session != null) {
//            if (memberService.findBySession(req) != null)
//                return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
//        }
//        Member member = memberRepository.findByName(seoul42.getLogin());
//        if (member == null)
//            throw new UnregisteredMemberException(seoul42);
//        session = req.getSession();
//        session.setAttribute("id", member.getId());
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }

    @Operation(summary = "logout API", description = "로그아웃",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content=@Content(schema = @Schema(implementation = Response.class))),
            })
    @GetMapping("/auth/logout")
    public ResponseEntity logout(HttpServletRequest req, HttpServletResponse res) {
//        res.addCookie(oven.burnCookie("ID"));
//        res.addCookie(oven.bakingMaxAge("0", 0));
//        session = req.getSession(false);
//        if (session != null)
//            session.invalidate();
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGOUT_SUCCESS), HttpStatus.OK);
    }
}
