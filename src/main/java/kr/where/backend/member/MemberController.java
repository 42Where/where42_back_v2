package kr.where.backend.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.member.dto.*;
import kr.where.backend.member.swagger.MemberApiDocs;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/member")
@RequiredArgsConstructor
public class MemberController implements MemberApiDocs {

	private final MemberService memberService;

	/**
	 * intraId에 해당하는 member 1명 조회
	 *
	 * @param intraId
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@GetMapping("/one")
	public ResponseEntity<ResponseMemberDTO> findOneByIntraId(@RequestParam("intraId") final Integer intraId) {
		final ResponseMemberDTO responseMemberDto = memberService.findOneByIntraId(intraId);

		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * 본인정보 조회
	 *
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@GetMapping("")
	public ResponseEntity findOneByAccessToken(@AuthUserInfo final AuthUser authUser) {
		final ResponseMemberDTO responseMemberDto = memberService.findOneByIntraId(authUser.getIntraId());

		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * DB에 존재하는 모든 멤버 list 조회
	 *
	 * @return ResponseEntity(ResponseMemberDTOList)
	 */
	@GetMapping("/all")
	public ResponseEntity<List<ResponseMemberDTO>> findAll() {
		final List<ResponseMemberDTO> responseMemberDTOList = memberService.findAll();

		return ResponseEntity.ok(responseMemberDTOList);
	}

	/**
	 * 본인 탈퇴
	 * accessToken을 받아서 claim에 존재하는 intraId에 해당하는 멤버 delete (본인 탈퇴)
	 *
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@DeleteMapping("")
	public ResponseEntity<ResponseMemberDTO> deleteMember(final HttpServletRequest request,
														  @AuthUserInfo final AuthUser authUser) {
		final ResponseMemberDTO responseMemberDto = memberService.deleteMember(request, authUser);
		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * 지정 멤버 탈퇴
	 * intraid를 path variable로 받아 삭제
	 *
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMemberDTO> deleteMemberByIntraId(@PathVariable("id") Integer intraId) {
		final ResponseMemberDTO responseMemberDto = memberService.deleteMemberByIntraId(intraId);
		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * 상태메세지 변경
	 *
	 * @param updateMemberCommentDto
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@PostMapping("/comment")
	public ResponseEntity<ResponseMemberDTO> updateComment(
			@RequestBody @Valid final UpdateMemberCommentDTO updateMemberCommentDto,
			@AuthUserInfo final AuthUser authUser) {

		final ResponseMemberDTO responseMemberDto = memberService.updateComment(updateMemberCommentDto, authUser);

		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * 상태메세지 삭제
	 *
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@DeleteMapping("/comment")
	public ResponseEntity<ResponseMemberDTO> deleteComment(@AuthUserInfo final AuthUser authUser) {

		final ResponseMemberDTO responseMemberDto = memberService.deleteComment(authUser);

		return ResponseEntity.ok(responseMemberDto);
	}
}