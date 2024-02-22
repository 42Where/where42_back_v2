package kr.where.backend.member.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.member.dto.ResponseMemberDTO;
import kr.where.backend.member.dto.UpdateMemberCommentDTO;
import kr.where.backend.member.exception.MemberException;

@Tag(name = "member", description = "member API")
public interface MemberApiDocs {

	@Operation(summary = "1.3 findOneByIntraId API", description = "맴버 1명 Dto 조회",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
			@Parameter(name = "intraId", description = "5자리 intra 고유 id", in = ParameterIn.QUERY),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@GetMapping("")
	ResponseEntity findOneByIntraId(@RequestParam("intraId") final Integer intraId);

	@Operation(summary = "1.6 findAll API", description = "모든 멤버 list 조회",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 조회 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@GetMapping("/all")
	ResponseEntity<List<ResponseMemberDTO>> findAll();

	@Operation(summary = "1.2 deleteMember API", description = "맴버 탈퇴",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseMemberDTO.class))),
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@DeleteMapping("")
	public ResponseEntity deleteMember(@AuthUserInfo final AuthUser authUser);

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
			@ApiResponse(responseCode = "1000", description = "존재하지 않는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.NoMemberException.class)))
		}
	)
	@PostMapping("/comment")
	public ResponseEntity updateComment(
		@RequestBody @Valid final UpdateMemberCommentDTO updateMemberCommentDto,
		@AuthUserInfo final AuthUser authUser);

	@Operation(summary = "1.5 createDummyAgreeMember API", description = "dummy 동의 맴버 생성 하는 POST API, dummy 멤버 10명 생성, 인자 필요 없음 (sign up 생기면 없어질 api 입니다!)",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		responses = {
			@ApiResponse(responseCode = "201", description = "맴버 생성 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseMemberDTO.class)))),
			@ApiResponse(responseCode = "1001", description = "이미 존재하는 맴버입니다.", content = @Content(schema = @Schema(implementation = MemberException.DuplicatedMemberException.class)))
		}
	)
	@PostMapping("/dummy")
	ResponseEntity<List<ResponseMemberDTO>> createDummyAgreeMembers();
}
