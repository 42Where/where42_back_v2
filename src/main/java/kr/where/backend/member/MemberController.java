package kr.where.backend.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.exception.customException.OutStateException;
import kr.where.backend.exception.customException.ServiceUnavailableException;
import kr.where.backend.exception.customException.TakenSeatException;
import kr.where.backend.member.DTO.*;
import kr.where.backend.utils.response.Response;
import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.ResponseWithData;
import kr.where.backend.utils.response.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Tag(name = "member", description = "member API")
@RequestMapping("/v3/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "createMember API", description = "맴버 생성 하는 POST API",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = CreateMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "201", description = "맴버 생성 성공")
		}
	)
	@PostMapping("/")
	public ResponseEntity createMember(@RequestBody CreateMemberDto createMemberDto) {

		final ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);

		return ResponseEntity.created(URI.create("http://3.35.149.29:8080/v3/main"))
			.body(responseMemberDto);
	}

	//    @GetMapping("/")
	//    public ResponseEntity findAllMember() {
	//        final List<ResponseMemberDto> responseMemberDtos = memberService.findAll();
	//
	//        return ResponseEntity.ok(responseMemberDtos);
	//    }

	@Operation(summary = "findOneByIntraId API", description = "맴버 1명 Dto 조회",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
			@Parameter(name = "intraId", description = "5자리 intra 고유 id", in = ParameterIn.QUERY),
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 조회 성공")
		}
	)
	@GetMapping("/")
	public ResponseEntity findOneByIntraId(@RequestParam Long intraId) {
		final ResponseMemberDto responseMemberDto = memberService.findOneByIntraId(intraId);

		return ResponseEntity.ok(responseMemberDto);
	}

	@Operation(summary = "deleteMember API", description = "맴버 탈퇴",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = DeleteMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 삭제 성공")
		}
	)
	@DeleteMapping("/")
	public ResponseEntity deleteMember(@RequestBody DeleteMemberDto deleteMemberDto) {

		final ResponseMemberDto responseMemberDto = memberService.deleteMember(deleteMemberDto);

		return ResponseEntity.ok(responseMemberDto);
	}

	@Operation(summary = "updatePersonalMessage API", description = "맴버 상태 메시지 변경",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = UpdateMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 상태 메시지 변경 성공")
		}
	)
	@PostMapping("/comment")
	public ResponseEntity updateComment(@RequestBody final UpdateMemberDto updateMemberDto) {
		final ResponseMemberDto responseMemberDto = memberService.updateComment(updateMemberDto);

		return ResponseEntity.ok(responseMemberDto);
	}

	@Operation(summary = "updateCustomLocation API", description = "맴버 수동자리 변경",
		parameters = {
			@Parameter(name = "accessToken", description = "인증/인가 확인용 accessToken", in = ParameterIn.HEADER),
		},
		requestBody =
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(schema = @Schema(implementation = UpdateMemberDto.class)))
		,
		responses = {
			@ApiResponse(responseCode = "200", description = "맴버 수동자리 변경 성공")
		}
	)
	@PostMapping("/custom-location")
	public ResponseEntity updateCustomLocation(@RequestBody final UpdateMemberDto updateMemberDto) {
		final ResponseMemberDto responseMemberDto = memberService.updateCustomLocation(updateMemberDto);

		return ResponseEntity.ok(responseMemberDto);
	}

	//    @Operation(summary = "get member information", description = "맴버 프로필과 현재 위치를 조회하는 API",
	//            parameters = {
	//                    @Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
	//            },
	//            responses = {
	//                    @ApiResponse(responseCode = "200", description = "맴버 정보에 대한 DTO 반환", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
	//                    @ApiResponse(responseCode = "401", description = "쿠키나 DB에 저장된 맴버의 토큰이 없을 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	//            }
	//
	//    )
	//    @GetMapping("/info")
	//    public ResponseEntity memberInformation(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
	//        String token42 = tokenService.findAccessToken(res, key);
	//        Member member = memberService.findBySessionWithToken(req, token42);
	//        log.info("[main] \"{}\"님이 메인화면을 조회하였습니다.", member.getName());
	//        if (member.timeDiff() < 1) {
	//            if (!Define.PARSED.equalsIgnoreCase(member.getLocation()))
	//                memberService.parseStatus(member, member.getLocate().getPlanet());
	//            return new MemberInfo(member);
	//        }
	//        memberService.parseStatus(member, token42);
	//        Member member = new Member("suhwpark", "http://localhost", "seocho", "single", MemberLevel.member);
	//        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, "조회 성공", new MemberInfo(member)), HttpStatus.OK);
	//    }

	//    @Operation(summary = "get member status message", description = "맴버 상태 메시지 조회 API",
	//            parameters = {
	//                    @Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
	//            },
	//            responses = {
	//                    @ApiResponse(responseCode = "200", description = "맴버의 상태 메시지 반환", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
	//                    @ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우",  content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	//                    @ApiResponse(responseCode = "401", description = "쿠키나 DB에 저장된 맴버의 토큰이 없을 경우",  content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	//            }
	//
	//    )
	//    @GetMapping("/status-msg")
	//    public ResponseEntity getPersonalMsg(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
	////        String token42 = tokenService.findAccessToken(res, key);
	////        Member member = memberService.findBySessionWithToken(req, token42);
	//        Member member = new Member("suhwpark", "http://localhost", "seocho", "single", MemberLevel.member);
	//        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, "조회 성공", member.getMsg()), HttpStatus.OK);
	//    }

	//    @Operation(summary = "post member personal status message", description = "맴버 상태 메시지 설정 API",
	//            parameters = {
	//                    @Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
	//            },
	//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	//                    content = @Content(schema = @Schema(implementation = Map.class))
	//            ),
	//            responses = {
	//                    @ApiResponse(responseCode = "200", description = "맴버의 상태 메시지 반환", content = @Content(schema = @Schema(implementation = String.class))),
	//                    @ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	//                    @ApiResponse(responseCode = "401", description = "쿠키나 DB에 저장된 맴버의 토큰이 없을 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	//            }
	//    )
	//    @PostMapping("/status-msg")
	//    public ResponseEntity updatePersonalMsg(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody Map<String, String> msg) {
	////        String token42 = tokenService.findAccessToken(res, key);
	////        memberService.updatePersonalMsg(req, token42, msg.get("msg"));
	//        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_MSG), HttpStatus.OK);
	//    }

	@Operation(summary = "get member location", description = "멤버 위치 설정 가능 여부 조회 API",
		parameters = {
			@Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "설정 가능 반환 여부", content = @Content(schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "맴버가 퇴근 상태이거나, 아이맥 자리 정보가 있을 시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "503", description = "24hane API 정상 정보 조회 불가능 시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		}

	)
	@GetMapping("/location")
	public ResponseEntity checkLocate(HttpServletRequest req, HttpServletResponse res,
		@CookieValue(value = "ID", required = false) String key)
		throws OutStateException, TakenSeatException, ServiceUnavailableException {
		//        String token42 = tokenService.findAccessToken(res, key);
		//        int planet = memberService.checkLocate(req, token42);
		int planet = 1;
		return new ResponseEntity(ResponseWithData.res(StatusCode.OK, ResponseMsg.NOT_TAKEN_SEAT, planet),
			HttpStatus.OK);
	}

	//    @Operation(summary = "post member location", description = "멤버 위치 설정 API",
	//            parameters = {
	//                    @Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
	//            },
	//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	//                    content = @Content(schema = @Schema(implementation = Locate.class))
	//            ),
	//            responses = {
	//                    @ApiResponse(responseCode = "200", description = "맴버 위치 설정 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
	//                    @ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	//            }
	//
	//    )
	//    @PostMapping("/location")
	//    public ResponseEntity updateLocate(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody Locate locate) {
	////        String token42 = tokenService.findAccessToken(res, key);
	////        Member member = memberService.findBySessionWithToken(req, token42);
	////        memberService.updateLocate(member, locate);
	////        log.info("[setting] \"{}\"님이 \"p:{}, f:{}, c:{}, s:{}\" (으)로 위치를 수동 변경하였습니다.", member.getName(),
	////                locate.getPlanet(), locate.getFloor(), locate.getCluster(), locate.getSpot());
	////        memberService.saveLocateDate(member.getName(), locate);
	//        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_LOCATE), HttpStatus.OK);
	//    }

	@Operation(summary = "post Eval button", description = "동료평가 정보 설정 API",
		parameters = {
			@Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "동료평가 상태 설정 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
			@ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "퇴근한 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		}

	)
	@PostMapping("/eval")
	public ResponseEntity updateEvalOn(HttpServletRequest req, HttpServletResponse res,
		@CookieValue(value = "ID", required = false) String key)
		throws OutStateException, ServiceUnavailableException {
		//        String token42 = tokenService.findAccessToken(res, key);
		//        Member member = memberService.findBySessionWithToken(req, token42);
		//        memberService.updateEvalOn(req, token42);
		//        log.info("[setting] \"{}\"님이 동료 평가 중으로 상태를 변경하였습니다.", member.getName());
		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.SET_EVAL_ON), HttpStatus.OK);
	}

	@Operation(summary = "create friend", description = "친구 생성 API",
		parameters = {
			@Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE),
			@Parameter(name = "friedName", description = "친구 카뎃 intra id", in = ParameterIn.QUERY),
			@Parameter(name = "imageUrl", description = "image URL", in = ParameterIn.QUERY)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "친구 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
			@ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "이미 등록된 친구", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		}

	)
	@PostMapping("/friend")
	public ResponseEntity createFriend(HttpServletRequest req, HttpServletResponse res,
		@CookieValue(value = "ID", required = false) String key, @RequestParam String friendName,
		@RequestParam String img) {
		//        String token42 = tokenService.findAccessToken(res, key);
		//        Member member = memberService.findBySessionWithToken(req, token42);
		//        if (memberRepository.checkFriendByMemberIdAndName(member.getId(), friendName))
		//            throw new RegisteredFriendException();
		//        Seoul42 friend = apiService.getUserInfo(token42, friendName);
		//        Long friendId = groupFriendService.saveFriend(friendName, img, friend.getCreated_at(), member.getDefaultGroupId());
		//        log.info("[friend] \"{}\"님이 \"{}\"님을 친구 추가 하였습니다", member.getName(), friendName);
		return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_GROUP_FRIEND, 2L),
			HttpStatus.CREATED);
	}

	@Operation(summary = "get all friends list", description = "모든 친구 조회 API",
		parameters = {
			@Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "친구 리스트 조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
			@ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@GetMapping("/friend")
	public ResponseEntity getAllDefaultFriends(HttpServletRequest req, HttpServletResponse res,
		@CookieValue(value = "ID", required = false) String key) {
		//        String token42 = tokenService.findAccessToken(res, key);
		//        Member member = memberService.findBySessionWithToken(req, token42);
		List<String> result = new ArrayList<>();
		return new ResponseEntity(ResponseWithData.res(StatusCode.OK, "친구 조회 성공", result), HttpStatus.OK);
	}

	@Operation(summary = "delete friends list", description = "친구 삭제 API",
		parameters = {
			@Parameter(name = "cookie", description = "DB 에서 맴버 조회를 위한 key get 용도", in = ParameterIn.COOKIE)
		},
		responses = {
			@ApiResponse(responseCode = "200", description = "친구 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
			@ApiResponse(responseCode = "401", description = "등록되지 않은 맴버일 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@DeleteMapping("/friend")
	public ResponseEntity deleteFriends(HttpServletRequest req, HttpServletResponse res,
		@CookieValue(value = "ID", required = false) String key, @RequestBody List<String> friendNames) {
		//        String token42 = tokenService.findAccessToken(res, key);
		//        Member member = memberService.findBySessionWithToken(req, token42);
		//        groupFriendService.deleteFriends(member, friendNames);
		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_GROUP_FRIENDS), HttpStatus.OK);
	}
}
