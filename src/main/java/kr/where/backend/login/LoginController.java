package kr.where.backend.login;

//@RestController
//@Tag(name = "login", description = "login")
//@RequestMapping("/v3/home")
//@Slf4j
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final TokenProvider tokenProvider;
//    @Operation(summary = "home API", description = "로그인 여부 조회",
//            parameters = {
//                    @Parameter(name = "key", description = "key in Token", in = ParameterIn.COOKIE)
//            },
//            responses = {
//                    @ApiResponse(responseCode = "201", description = "로그인 성공", content = @Content(schema = @Schema(implementation = Response.class))),
//                    @ApiResponse(responseCode = "401", description = "등록되지 않은 멤버", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            })
//    @GetMapping("/")
//    public ResponseEntity home(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
////        token = tokenService.findAccessToken(res, key);
////        memberService.findBySessionWithToken(req, token);
//        String token = tokenProvider.createToken(2L);
//        log.info(token);
//        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS), HttpStatus.OK);
//    }
//}
