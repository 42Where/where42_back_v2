package kr.where.backend.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import java.util.List;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberListDTO;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.utils.response.ResponseWithData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/group")
@AllArgsConstructor
@Tag(name = "group", description = "group API")
public class GroupController {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    @Operation(
        summary = "create new group API",
        description = "그룹 생성",
        parameters = {
            @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
        },
        requestBody = @RequestBody(description = "그룹 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 201, \"responseMsg\": \"그룹 생성 성공\", \"data\": [\"groupId\", \"groupName\"]}"),})),
            @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
            @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
            @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example3", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
        }
    )
    @PostMapping("/")
    public ResponseEntity createGroup(@RequestBody @Valid CreateGroupDto request){
        ResponseGroupDto dto = groupService.createGroup(request);
//        CreateGroupMemberDTO groupMemberDTO = CreateGroupMemberDTO.builder()
//                .groupId(dto.getGroupId())
//                .intraId(request.getMemberIntraId())
//                .groupName(request.getGroupName())
//                .isOwner(true)
//                .build();
//        groupMemberService.createGroupMember(groupMemberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
        summary = "get group list with friend API",
        description = "멤버가 만든 그룹 & 그룹 내의 친구 리스트 조회(메인화면 용)",
        parameters = {
            @Parameter(name = "id", description = "멤버 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.QUERY)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [ {\"groupId\": 2, \"groupName\": \"즐겨찾기\", \"count\": 0, \"groupFriends\": [] }, {\"groupId\": 3, \"groupName\": \"그룹1\", \"count\": 2, \"groupFriends\": [\"친구1\", \"친구2\"] }, {\"groupId\": 4, \"groupName\": \"그룹2\", \"count\": 1, \"groupFriends\": [\"친구1\"] } ] }"),})),
        }
    )
    @GetMapping("/")
    public ResponseEntity findGroups(@RequestBody @Valid RequestGroupMemberDTO request){
        List<ResponseGroupMemberListDTO> dto =  groupMemberService.findGroupMembers(request);
        // TODO GroupMember
        /* 메인화면에 띄우는 것 : 위치 / 사진 / intraName / 상태메세지 */

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        summary = "modify group name API",
        description = "그룹 이름 수정",
        parameters = {
            @Parameter(name = "groupId", description = "이름을 변경할 그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
        },
        requestBody = @RequestBody(description = "변경할 새로운 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "그룹 이름 변경 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 이름 변경 성공\", \"data\": [{\"groupId\": 3, \"changeName\": \"그룹1\"}] }"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example2", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example3", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
        }
    )
    @PostMapping("/")
    public ResponseEntity updateGroup(@RequestBody @Valid UpdateGroupDto dto){
        ResponseGroupDto responseGroupDto = groupService.updateGroup(dto);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @Operation(
        summary = "delete group API",
        description = "그룹 삭제",
        parameters = {
            @Parameter(name = "groupId", description = "삭제할 그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 삭제 성공\", \"data\": [ {\"groupId\": 3} ] }"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
        }
    )
    @DeleteMapping("/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId){
        RequestGroupMemberDTO dto = RequestGroupMemberDTO.builder().groupId(groupId).build();
        groupMemberService.deleteGroupMember(dto);
        ResponseGroupDto responseGroupDto = groupService.deleteGroup(groupId);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @Operation(
        summary = "get group list API",
        description = "그룹별 id, 이름 반환 (그룹관리)",
        parameters = {
            @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [ { \"groupId\": 2, \"groupName\": \"즐겨찾기\" }, { \"groupId\": 3, \"groupName\": \"그룹1\" } ] }")})),
            @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
            @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
        }
    )
    @GetMapping("/names/{memberIntraId}")
    public ResponseEntity findGroupNames(@PathVariable("memberIntraId") Long memberIntraId) {
        // TODO Group

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(
        summary = "get not included friends in group API",
        description = "그룹에 포함되지 않은 친구 목록을 조회 (그룹관리)",
//        description = "그룹에 포함되지 않은 친구 목록을 조회. 그룹에 새로운 친구를 추가하기 위함이다. 이때, 조회되는 친구들은 멤버가 친구로 등록하되 해당 그룹에 등록되지 않은 친구들이다.",
        parameters = {
            @Parameter(name = "groupId", description = "그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
            @Parameter(name = "key", description = "Token 내의 ID 값", required = false, in = ParameterIn.COOKIE)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [\"친구1\", \"친구2\", \"친구3\"]}")})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"존재하지 않는 그룹\"}")})),
            @ApiResponse(responseCode = "401", description = "토큰 쿠키 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"토큰 쿠키 찾을 수 없음\"}")})),
            @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
        }
    )
    @GetMapping("/{groupId}/not/friends")
    public ResponseEntity<List<String>> findMemberListNotInGroup(@PathVariable("groupId") Long groupId) {
        // TODO GroupMember
        /* 친구(기본그룹) 중 groupId 그룹에 포함되지 않은 친구 intraName List 반환  */

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
        summary = "add friends to group API",
        description = "친구 리스트를 받아서 해당 그룹에 일괄 추가",
        parameters = {
            @Parameter(name = "groupId", description = "친구를 추가할 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
        },
        requestBody = @RequestBody(description = "추가하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "친구 일괄 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
              @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
        }
    )
    @PostMapping("/{groupId}/friends")
    public ResponseEntity addFriendsToGroup(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
        // TODO GroupMember
        /* false 로 생성하는 그거 */
        /* dto 로 받는게 나을것 같기두*/

    return new ResponseEntity(HttpStatus.CREATED);
    }

    @Operation(
        summary = "get group friend list API",
        description = "그룹 내의 모든 친구 목록 조회",
        //      description = "멤버가 등록한 그룹 내의 모든 친구 목록 조회",
        parameters = {
            @Parameter(name = "groupId", description = "조회를 원하는 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{ \"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [\"친구1\", \"친구2\", \"친구3\"] }"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
        }
    )
    @GetMapping("/{groupId}/friends")
    public ResponseEntity<List<String>> getIncludeGroupFriendNames(@PathVariable("groupId") Long groupId) {
        // TODO GroupMember
        /* intraName list로 반환하면 될거같죠? */
        /* 이름도 싹다 find 나 get 중 하나로 통일하면 좋을 것 같아요! */

    return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
        summary = "delete group friend API",
        description = "친구 리스트를 받아서 해당 그룹에서 일괄 삭제함. 이때, 친구 리스트는 모두 해당 그룹에 속해있어야 함",
        parameters = {
            @Parameter(name = "groupId", description = "그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
        },
        requestBody = @RequestBody(description = "삭제하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹에서 친구 삭제 성공\"}"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
        }
    )
    @DeleteMapping("/{groupId}/friends")
    public ResponseEntity removeIncludeGroupFriends(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
        // TODO GroupMember
        /* 여기도 dto 로 받는게 나을것 같기두*/

        return new ResponseEntity(HttpStatus.OK);
    }
}
