package kr.where.backend.login;

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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "login", description = "login")
@RequestMapping("/v3/home")
public class LoginController {

    @Operation(summary = "home API", description = "로그인 여부 조회",
            parameters = {
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "로그인 성공", content=@Content(schema = @Schema(implementation = Response.class))),
                    @ApiResponse(responseCode = "401", description = "등록되지 않은 멤버", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/")
    public ResponseEntity home(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
//        token = tokenService.findAccessToken(res, key);
//        memberService.findBySessionWithToken(req, token);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
    }
}
