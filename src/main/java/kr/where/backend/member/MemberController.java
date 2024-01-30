package kr.where.backend.member;

import jakarta.validation.Valid;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
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

	@GetMapping("")
	public ResponseEntity findOneByIntraId(@RequestParam("intraId") final Integer intraId) {
		final ResponseMemberDTO responseMemberDto = memberService.findOneByIntraId(intraId);

		return ResponseEntity.ok(responseMemberDto);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ResponseMemberDTO>> findAll() {
		final List<ResponseMemberDTO> responseMemberDTOList = memberService.findAll();

		return ResponseEntity.ok(responseMemberDTOList);
	}

	@DeleteMapping("")
	public ResponseEntity deleteMember() {
		final AuthUserInfo authUser = AuthUserInfo.of();
		final ResponseMemberDTO responseMemberDto = memberService.deleteMember(authUser);

		return ResponseEntity.ok(responseMemberDto);
	}

	@PostMapping("/comment")
	public ResponseEntity updateComment(@RequestBody @Valid final UpdateMemberCommentDTO updateMemberCommentDto) {
		final AuthUserInfo authUser = AuthUserInfo.of();
		final ResponseMemberDTO responseMemberDto = memberService.updateComment(updateMemberCommentDto, authUser);

		return ResponseEntity.ok(responseMemberDto);
	}

	@PostMapping("/dummy")
	public ResponseEntity<List<ResponseMemberDTO>> createDummyAgreeMembers() {
		List<ResponseMemberDTO> responseMemberDTOList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			CadetPrivacy cadetPrivacy = CadetPrivacy.create(1 + i, "member" + i, "c1r1s" + i,
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
