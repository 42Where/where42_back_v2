package kr.where.backend.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.utils.response.ResponseWithData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "event", description = "")
@RequestMapping("/v3/event")
public class EventController {
    @Operation(summary = "checkEvent API", description = "이벤트 param 및 당첨 결과 확인",
            parameters = {
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE),
                    @Parameter(name="param", description = "확인할 이벤트 param", in= ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "당첨 여부 반환 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class))),
                    @ApiResponse(responseCode = "400", description = "이벤트와 일치하지 않는 param", content=@Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/")
    public ResponseEntity checkEvent(HttpServletRequest req, HttpServletResponse res,
                                     @CookieValue(value = "ID", required = false) String key, @RequestParam String param) {
//        String token42 = tokenService.findAccessToken(res, key);
//        Member member = memberService.findBySessionWithToken(req, token42);
//        String place;
//        if (param.equals("khq5n6r7"))
//            place = "A";
//        else if (param.equals("ui3zzgbl"))
//            place = "B";
//        else if (param.equals("0z3oprmj"))
//            place = "C";
//        else if (param.equals("7cz3ex68"))
//            place = "D";
//        else if (param.equals("z51m8l8e"))
//            place = "E";
//        else if (param.equals("barh6z2c"))
//            place = "F";
//        else if (param.equals("4rq01zpy"))
//            place = "G";
//        else if (param.equals("gfppifr1"))
//            place = "H";
//        else if (param.equals("olsmt445"))
//            place = "I";
//        else if (param.equals("t8w0193r"))
//            place = "J";
//        else
//            throw new NotFoundException();
//        int ret = eventService.checkEvent(member.getName(), place);
//        return ret;
        int result = 1;
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
