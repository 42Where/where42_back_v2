package kr.where.backend.join;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.api.exception.JsonException;
import kr.where.backend.join.dto.ResponseJoin;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/join")
@Tag(name = "join", description = "join API")
public class JoinController {
    private final JoinService joinService;

    @Operation(summary = "3.1 JoinMember API", description = "동의 맴버 생성하는 Post API",
            parameters = {
                    @Parameter(name = "login",
                            description = "OAuth2 login한 카뎃의 intra Name", in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 생성 성공",
                            content = @Content(schema = @Schema(implementation = ResponseJoin.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 생성 실패",
                            content = @Content(schema = @Schema(implementation = JsonException.class)))
            }

    )
    @PostMapping("/{intraId}")
    public ResponseEntity<HttpStatus> join(@PathVariable("intraId") final Integer intraId) {
        joinService.join(intraId);

        //프런트 상의 create는 201이까요
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
