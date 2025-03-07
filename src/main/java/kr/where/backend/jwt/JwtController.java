package kr.where.backend.jwt;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.where.backend.jwt.dto.RequestReissueDTO;
import kr.where.backend.jwt.dto.ResponseAccessTokenDTO;
import kr.where.backend.jwt.swagger.JwtApiDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/jwt")
@RequiredArgsConstructor
public class JwtController implements JwtApiDocs {
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseAccessTokenDTO> reIssue(final HttpServletResponse response,
                                                          @RequestBody @Valid final RequestReissueDTO requestReissueDTO)
    {
        return ResponseEntity.ok(
                jwtService.reissueAccessToken(
                                response,
                                requestReissueDTO.getIntraId()
                            )
        );
    }
}
