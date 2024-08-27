package kr.where.backend.jwt;

import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import kr.where.backend.jwt.swagger.JwtApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/jwt")
@RequiredArgsConstructor
public class JwtController implements JwtApiDocs {
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseAccessTokenDTO> reIssue(
            final HttpServletResponse response,
            @CookieValue(value = "refreshToken") final String refreshToken
    )
    {

        return ResponseEntity.ok(
                jwtService.reissueAccessToken(response, refreshToken)
        );
    }
}
