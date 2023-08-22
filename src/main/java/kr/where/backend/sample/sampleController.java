package kr.where.backend.sample;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="sample", description = "sample API group")
public class sampleController {


    @Operation(summary = "createSample API", description = "post api 샘플입니다",
            parameters = {
                    @Parameter(name="req", description = "request for session", in= ParameterIn.HEADER),
                    @Parameter(name="res", description = "response for session", in= ParameterIn.HEADER),
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE),
                    @Parameter(name="sampleName", description = "request for session", in= ParameterIn.QUERY)
            },
            responses = {
                @ApiResponse(responseCode = "201", description = "post 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class)))
            })
    @PostMapping("/sample")
    public ResponseEntity createSample(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestParam("sampleName") String sampleName) {
        int groupId = 1;
        return new ResponseEntity(ResponseWithData.res(201, ResponseMsg.CREATE_GROUP, groupId), HttpStatus.CREATED);
    }

    @Operation(summary = "getSample API", description = "get api 샘플입니다",
            parameters = {
                    @Parameter(name="req", description = "request for session", in= ParameterIn.HEADER),
                    @Parameter(name="res", description = "response for session", in= ParameterIn.HEADER),
                    @Parameter(name="key", description = "key in Token", in= ParameterIn.COOKIE),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "get 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class)))
            })
    @GetMapping("/sample")
    public ResponseEntity getSample(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        int groupId = 1;
        return new ResponseEntity(ResponseWithData.res(200, ResponseMsg.CREATE_GROUP, groupId), HttpStatus.OK);
    }

    @Operation(summary = "deleteSample API", description = "delete api 샘플입니다",
            parameters = {
                    @Parameter(name="sampleId", description = "샘플 id", in= ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "delete 성공", content=@Content(schema = @Schema(implementation = ResponseWithData.class)))
            })
    @DeleteMapping("/sample/{sampleId}")
    public ResponseEntity deleteSample(@PathVariable("sampleId") Long sampleId) {
        return new ResponseEntity(Response.res(200, ResponseMsg.DELETE_GROUP), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.notFound().build();
    }
}