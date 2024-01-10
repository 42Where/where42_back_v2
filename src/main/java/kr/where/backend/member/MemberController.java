package kr.where.backend.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.member.dto.*;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "member", description = "member API")
@RequestMapping("/v3/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "1.1 createMember API", description = "동의 맴버 생성 하는 POST API (sign up 생기면 없어질 api 입니다!)",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "example",
                                            value = "{\n" +
                                                    "  \"cadetPrivacy\": {\n" +
                                                    "    \"id\": 12345,\n" +
                                                    "    \"login\": \"string\",\n" +
                                                    "    \"location\": \"string\",\n" +
                                                    "    \"image\": {\n" +
                                                    "      \"versions\": {\n" +
                                                    "        \"small\": \"string\"\n" +
                                                    "      }\n" +
                                                    "    },\n" +
                                                    "    \"created_at\": \"string\",\n" +
                                                    "    \"active?\": true\n" +
                                                    "  },\n" +
                                                    "  \"hane\": {\n" +
                                                    "    \"login\": \"string\",\n" +
                                                    "    \"inoutState\": \"string\",\n" +
                                                    "    \"cluster\": \"string\",\n" +
                                                    "    \"tag_at\": \"string\"\n" +
                                                    "  }\n" +
                                                    "}"                                    )
                            },
                            schema = @Schema(type = "object")
                    )
            )
            ,
            responses = {
                    @ApiResponse(responseCode = "201", description = "맴버 생성 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 생성 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @PostMapping("")
    public ResponseEntity createAgreeMember(@RequestBody ObjectNode saveObj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        CadetPrivacy cadetPrivacy = mapper.treeToValue(saveObj.get("cadetPrivacy"), CadetPrivacy.class);
        Hane hane = mapper.treeToValue(saveObj.get("hane"), Hane.class);

        // 나머지 로직은 동일
        final Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        final ResponseMemberDTO responseMemberDto = ResponseMemberDTO.builder().member(member).build();

        return ResponseEntity.created(URI.create("http://3.35.149.29:8080/v3/main"))
                .body(responseMemberDto);
    }

    @Operation(summary = "1.3 findOneByIntraId API", description = "맴버 1명 Dto 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
                    @Parameter(name = "intraId", description = "5자리 intra 고유 id", in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @GetMapping("")
    public ResponseEntity findOneByIntraId(@RequestParam("intraId") final Integer intraId) {
        final ResponseMemberDTO responseMemberDto = memberService.findOneByIntraId(intraId);

        return ResponseEntity.ok(responseMemberDto);
    }

    @Operation(summary = "1.6 findAll API", description = "모든 멤버 list 조회",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<ResponseMemberDTO>> findAll() {
        final List<ResponseMemberDTO> responseMemberDTOList = memberService.findAll();

        return ResponseEntity.ok(responseMemberDTOList);
    }

    @Operation(summary = "1.2 deleteMember API", description = "맴버 탈퇴",
            parameters = {
                @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
                @Parameter(name = "intraId", description = "5자리 intra 고유 id", in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 삭제 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @DeleteMapping("")
    public ResponseEntity deleteMember() {
        final AuthUserInfo authUser = AuthUserInfo.of();
        // 본인인지 확인 여부가 필요할 듯!
        final ResponseMemberDTO responseMemberDto = memberService.deleteMember(authUser);

        return ResponseEntity.ok(responseMemberDto);
    }

    @Operation(summary = "1.4 updatePersonalMessage API", description = "맴버 상태 메시지 변경",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UpdateMemberCommentDTO.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "맴버 상태 메시지 변경 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
                    @ApiResponse(responseCode = "404", description = "맴버 상태 메시지 설정 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @PostMapping("/comment")
    public ResponseEntity updateComment(@RequestBody @Valid final UpdateMemberCommentDTO updateMemberCommentDto) {
        final AuthUserInfo authUser = AuthUserInfo.of();
        final ResponseMemberDTO responseMemberDto = memberService.updateComment(updateMemberCommentDto, authUser);

        return ResponseEntity.ok(responseMemberDto);
    }

    @Operation(summary = "1.5 createDummyAgreeMember API", description = "dummy 동의 맴버 생성 하는 POST API, dummy 멤버 10명 생성, 인자 필요 없음 (sign up 생기면 없어질 api 입니다!)",
            parameters = {
                    @Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "맴버 생성 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseMemberDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "맴버 생성 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
            }
    )
    @PostMapping("/dummy")
    public ResponseEntity<List<ResponseMemberDTO>> createDummyAgreeMembers() {
        List<ResponseMemberDTO> responseMemberDTOList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(1 + i, "member" + i, "c1r1s" + i, "https://ibb.co/94KmxcT", true, "2022-10-31");
            Hane hane = Hane.createForTest("IN");

            Member member = memberService.createAgreeMember(cadetPrivacy, hane);
            ResponseMemberDTO responseMemberDto = ResponseMemberDTO.builder().member(member).build();
            responseMemberDTOList.add(responseMemberDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("http://3.35.149.29:8080/v3/main"))
                .body(responseMemberDTOList);
    }
}
