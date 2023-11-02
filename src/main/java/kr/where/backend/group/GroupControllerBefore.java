//package kr.where.backend.group;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.media.ArraySchema;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.ExampleObject;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.List;
//
//import kr.where.backend.utils.response.Response;
//import kr.where.backend.utils.response.ResponseMsg;
//import kr.where.backend.utils.response.ResponseWithData;
//import kr.where.backend.utils.response.StatusCode;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/v3/group")
//@Tag(name = "group", description = "group API")
//public class GroupControllerBefore {
//
//  @Operation(
//      summary = "create new group API",
//      description = "그룹 생성",
//      parameters = {
//          @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
//      },
//      requestBody = @RequestBody(description = "그룹 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
//      responses = {
//          @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 201, \"responseMsg\": \"그룹 생성 성공\", \"data\": [\"groupId\", \"groupName\"]}"),})),
//          @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
//          @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
//          @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example3", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
//      }
//  )
//  @PostMapping("/")
//  public ResponseEntity createCustomGroup(HttpServletRequest req, HttpServletResponse res,
//      @CookieValue(value = "ID", required = false) String key,
//      @RequestBody String groupName) {
//    int groupId = 1;
//    return new ResponseEntity(
//        ResponseWithData.res(
//            StatusCode.CREATED,
//            ResponseMsg.CREATE_GROUP, groupId),
//        HttpStatus.CREATED);
//  }
//
//  @Operation(
//      summary = "get group list with friend API",
//      description = "멤버가 만든 그룹 & 그룹 내의 친구 리스트 조회(메인화면 용)",
//      parameters = {
//          @Parameter(name = "id", description = "멤버 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.QUERY)
//      },
//      responses = {
//          @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [ {\"groupId\": 2, \"groupName\": \"즐겨찾기\", \"count\": 0, \"groupFriends\": [] }, {\"groupId\": 3, \"groupName\": \"그룹1\", \"count\": 2, \"groupFriends\": [\"친구1\", \"친구2\"] }, {\"groupId\": 4, \"groupName\": \"그룹2\", \"count\": 1, \"groupFriends\": [\"친구1\"] } ] }"),})),
//      }
//  )
//  @GetMapping("/")
//  public void getGroupsWithFriend(
//      @RequestParam Long id) {
//    // List<MemberGroupInfo>를 반환
//    return;
//  }
//
//
//  @Operation(
//      summary = "get group list API",
//      description = "기본 그룹을 제외하고 멤버가 가지고 있는 모든 그룹(즐겨찾기 포함) 반환",
//      parameters = {
//          @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
//      },
//      responses = {
//          @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [ { \"groupId\": 2, \"groupName\": \"즐겨찾기\" }, { \"groupId\": 3, \"groupName\": \"그룹1\" } ] }")})),
//          @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
//          @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
//      }
//  )
//  @GetMapping("/list")
//  public void getGroupsExceptDefault(
//      HttpServletRequest req, HttpServletResponse res,
//      @CookieValue(value = "ID", required = false) String key) {
//    //List<GroupDto>를 반환
//    return;
//  }
//
//  @Operation(
//      summary = "modify group name API",
//      description = "그룹 이름 수정",
//      parameters = {
//          @Parameter(name = "groupId", description = "이름을 변경할 그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
//      },
//      requestBody = @RequestBody(description = "변경할 새로운 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
//      responses = {
//          @ApiResponse(responseCode = "200", description = "그룹 이름 변경 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 이름 변경 성공\", \"data\": [{\"groupId\": 3, \"changeName\": \"그룹1\"}] }"),})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example2", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
//          @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example3", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
//      }
//  )
//  @PatchMapping("/{groupId}")
//  public ResponseEntity updateGroupName(@PathVariable("groupId") Long groupId,
//      @RequestBody String changeName) {
//    return new ResponseEntity(
//        ResponseWithData.res(
//            StatusCode.OK,
//            ResponseMsg.CHANGE_GROUP_NAME, groupId),
//        HttpStatus.OK);
//  }
//
//
//  @Operation(
//      summary = "delete group API",
//      description = "그룹 삭제",
//      parameters = {
//          @Parameter(name = "groupId", description = "삭제할 그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
//      },
//      responses = {
//          @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 삭제 성공\", \"data\": [ {\"groupId\": 3} ] }"),})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
//      }
//  )
//  @DeleteMapping("/{groupId}")
//  public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId) {
//    return new ResponseEntity(
//        Response.res(
//            StatusCode.OK,
//            ResponseMsg.DELETE_GROUP), HttpStatus.OK);
//  }
//
//  @Operation(
//      summary = "get not included friends in group API",
//      description = "그룹에 포함되지 않은 친구 목록을 조회. 그룹에 새로운 친구를 추가하기 위함이다. 이때, 조회되는 친구들은 멤버가 친구로 등록하되 해당 그룹에 등록되지 않은 친구들이다.",
//      parameters = {
//          @Parameter(name = "groupId", description = "그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
//          @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
//      },
//      responses = {
//          @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [\"친구1\", \"친구2\", \"친구3\"]}")})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"존재하지 않는 그룹\"}")})),
//          @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
//          @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
//      }
//  )
//  @GetMapping("/{groupId}/friend/not-include/")
//  public ResponseEntity<List<String>> getNotIncludeGroupFriendNames(
//      HttpServletRequest req,
//      HttpServletResponse res,
//      @CookieValue(value = "ID", required = false) String key,
//      @PathVariable("groupId") Long groupId
//  ) {
//    List<String> ret = null;
//    ret.add("친구1");
//    return new ResponseEntity<>(ret, HttpStatus.OK);
//  }
//
//  @Operation(
//      summary = "add friends to group API",
//      description = "친구 리스트를 받아서 해당 그룹에 일괄 추가",
//      parameters = {
//          @Parameter(name = "groupId", description = "친구를 추가할 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
//      },
//      requestBody = @RequestBody(description = "추가하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
//      ),
//      responses = {
//          @ApiResponse(responseCode = "201", description = "친구 일괄 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
//      }
//  )
//  @PostMapping("/{groupId}/friend")
//  public ResponseEntity addFriendsToGroup(@PathVariable("groupId") Long groupId,
//      @RequestBody List<String> friendNames) {
//    return new ResponseEntity(
//       Response.res(
//           StatusCode.CREATED,
//            ResponseMsg.ADD_FRIENDS_TO_GROUP),
//        HttpStatus.CREATED);
//  }
//
//  @Operation(
//      summary = "get group friend list API",
//      description = "멤버가 등록한 그룹 내의 모든 친구 목록 조회",
//      parameters = {
//          @Parameter(name = "groupId", description = "조회를 원하는 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
//      },
//      responses = {
//          @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{ \"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [\"친구1\", \"친구2\", \"친구3\"] }"),})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
//      }
//  )
//  @GetMapping("/{groupId}/friend")
//  public ResponseEntity<ResponseWithData<List<String>>> getIncludeGroupFriendNames(
//      @PathVariable("groupId") Long groupId) {
//    List<String> ret = null;
//    ret.add("친구 1");
//
//    ResponseWithData<List<String>> response = ResponseWithData.res(
//       StatusCode.OK,
//        "친구 조회 성공", ret);
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
//
//  @Operation(
//      summary = "delete group friend API",
//      description = "친구 리스트를 받아서 해당 그룹에서 일괄 삭제함. 이때, 친구 리스트는 모두 해당 그룹에 속해있어야 함",
//      parameters = {
//          @Parameter(name = "groupId", description = "그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
//      },
//      requestBody = @RequestBody(description = "삭제하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
//      ),
//      responses = {
//          @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹에서 친구 삭제 성공\"}"),})),
//          @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
//              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
//      }
//  )
//  @DeleteMapping("/{groupId}/friend")
//  public ResponseEntity removeIncludeGroupFriends(@PathVariable("groupId") Long groupId,
//      @RequestBody List<String> friendNames) {
//    return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_FRIENDS_FROM_GROUP),
//        HttpStatus.OK);
//  }
//}
