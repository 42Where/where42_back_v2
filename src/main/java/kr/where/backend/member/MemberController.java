package kr.where.backend.member;

import jakarta.validation.Valid;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import kr.where.backend.member.dto.*;
import kr.where.backend.member.swagger.MemberApiDocs;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
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
	@GetMapping("")
	public ResponseEntity findOneByIntraId(@RequestParam("intraId") final Integer intraId) {
		final ResponseMemberDTO responseMemberDto = memberService.findOneByIntraId(intraId);

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
	 * 멤버 탈퇴
	 * accessToken을 받아서 claim에 존재하는 intraId에 해당하는 멤버 delete (본인 탈퇴)
	 *
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@DeleteMapping("")
	public ResponseEntity deleteMember(@AuthUserInfo final AuthUser authUser) {
		final ResponseMemberDTO responseMemberDto = memberService.deleteMember(authUser);

		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * 상태메세지 변경
	 *
	 * @param updateMemberCommentDto
	 * @return ResponseEntity(ResponseMemberDTO)
	 */
	@PostMapping("/comment")
	public ResponseEntity updateComment(
			@RequestBody @Valid final UpdateMemberCommentDTO updateMemberCommentDto,
			@AuthUserInfo final AuthUser authUser) {

		final ResponseMemberDTO responseMemberDto = memberService.updateComment(updateMemberCommentDto, authUser);

		return ResponseEntity.ok(responseMemberDto);
	}

	/**
	 * dummy member 10명 생성
	 *
	 * @return ResponseEntity(ResponseMemberDTOList)
	 * @deprecated test용 dummy
	 */
	@PostMapping("/dummy")
	public ResponseEntity<List<ResponseMemberDTO>> createDummyAgreeMembers() {
		List<ResponseMemberDTO> responseMemberDTOList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			CadetPrivacy cadetPrivacy = new CadetPrivacy(1 + i, "member" + i, "c1r1s" + i,
				"https://ibb.co/94KmxcT", true, "2022-10-31");
			Hane hane = Hane.create("IN");

			Member member = memberService.createAgreeMember(cadetPrivacy, hane);
			ResponseMemberDTO responseMemberDto = ResponseMemberDTO.builder().member(member).build();
			responseMemberDTOList.add(responseMemberDto);
		}

		return ResponseEntity.status(HttpStatus.CREATED)
			.location(URI.create("http://3.35.149.29:8080/v3/main"))
			.body(responseMemberDTOList);
	}
}
