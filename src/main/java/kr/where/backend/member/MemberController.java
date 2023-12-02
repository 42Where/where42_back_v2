package kr.where.backend.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.member.dto.*;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
@Tag(name = "member", description = "member API")
@RequestMapping("/v3/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "1.3 findOneByIntraId API", description = "맴버 1명 Dto 조회",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
			@Parameter(name = "intraId", description = "5자리 intra 고유 id", in = ParameterIn.QUERY),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDto.class))),
			@ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
		}
	)
	@GetMapping("/")
	public ResponseEntity findOneByIntraId(@RequestParam Long intraId) {
		final ResponseMemberDto responseMemberDto = memberService.findOneByIntraId(intraId);

		return ResponseEntity.ok(responseMemberDto);
	}

	@Operation(summary = "1.2 deleteMember API", description = "맴버 탈퇴",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = DeleteMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDto.class))),
			@ApiResponse(responseCode = "404", description = "맴버 삭제 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
		}
	)
	@DeleteMapping("/")
	public ResponseEntity deleteMember(@RequestBody DeleteMemberDto deleteMemberDto) {

		final ResponseMemberDto responseMemberDto = memberService.deleteMember(deleteMemberDto);

		return ResponseEntity.ok(responseMemberDto);
	}

	@Operation(summary = "1.4 updatePersonalMessage API", description = "맴버 상태 메시지 변경",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = UpdateMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 상태 메시지 변경 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDto.class))),
			@ApiResponse(responseCode = "404", description = "맴버 상태 메시지 설정 실패", content = @Content(schema = @Schema(implementation = MemberException.class)))
		}
	)
	@PostMapping("/comment")
	public ResponseEntity updateComment(@RequestBody final UpdateMemberDto updateMemberDto) {
		final ResponseMemberDto responseMemberDto = memberService.updateComment(updateMemberDto);

		return ResponseEntity.ok(responseMemberDto);
	}
}
