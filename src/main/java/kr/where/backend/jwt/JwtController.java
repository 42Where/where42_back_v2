package kr.where.backend.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.jwt.dto.ResponseRefreshTokenDTO;
import kr.where.backend.jwt.swagger.JwtApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/jwt")
@RequiredArgsConstructor
public class JwtController implements JwtApiDocs {
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseRefreshTokenDTO> reIssue(@AuthUserInfo AuthUser authUser) {

        return ResponseEntity.ok(jwtService.reissueAccessToken(authUser));
    }
}
