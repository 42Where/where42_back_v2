package kr.where.backend.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import java.util.List;

import kr.where.backend.group.dto.group.FindGroupDto;
import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.utils.response.ResponseWithData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

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
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "그룹 생성 요청",
                required = true, content = @Content(schema = @Schema(implementation = CreateGroupDto.class))),
        responses = {
            @ApiResponse(responseCode = "201", description = "그룹 생성 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 201, \"responseMsg\": \"그룹 생성 성공\", \"data\": [\"groupId\", \"groupName\"]}"),})),
            @ApiResponse(responseCode = "2001", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example3", value = "{\"statusCode\": 2001, \"responseMsg\": \"그룹 이름 중복\"}"),})),
        }
    )
    @PostMapping("/")
    public ResponseEntity createGroup(@RequestBody CreateGroupDto request){
        ResponseGroupDto dto = groupService.createGroup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    //완성

    @Operation(
        summary = "get group list with friend API",
        description = "멤버가 만든 그룹 & 그룹 내의 친구 리스트 조회(메인화면 용)",
        parameters = {
            @Parameter(name = "id", description = "멤버 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.QUERY)
        },
        responses = {@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class),
                examples = {@ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"조회 성공\", \"data\": [ {\"groupId\": 2, \"groupName\": \"기본그룹\", \"count\": 3, \"groupFriends\": [\"친구1\", \"친구2\", \"친구3\"] }," +
                        " {\"groupId\": 3, \"groupName\": \"그룹1\", \"count\": 2, \"groupFriends\": [\"친구1\", \"친구2\"] }, {\"groupId\": 4, \"groupName\": \"그룹2\", \"count\": 1, \"groupFriends\": [\"친구1\"] } ] }"
                )}))
        }
    )
    @GetMapping("/")
    public ResponseEntity findAllGroups(@RequestParam Long memberId){
        System.out.println("memberId : " + memberId);
        List<ResponseGroupMemberListDTO> dto =  groupMemberService.findAllGroupInformation(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }//완성 아마두

    @Operation(
        summary = "modify group name API",
        description = "그룹 이름 수정",
        parameters = {
            @Parameter(name = "groupId", description = "이름을 변경할 그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "변경할 새로운 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
        responses = {
            @ApiResponse(responseCode = "200", description = "그룹 이름 변경 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 이름 변경 성공\", \"data\": [{\"groupId\": 3, \"changeName\": \"그룹1\"}] }"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example2", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                @ExampleObject(name = "example3", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
        }
    )
    @PostMapping("/name/")
    public ResponseEntity updateGroup(@RequestBody @Valid UpdateGroupDto dto){
        ResponseGroupDto responseGroupDto = groupService.updateGroup(dto);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }
    //완성, 근데 이거도 비즈니스 로직으로 만들어야 겟습니다요?

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
    @DeleteMapping("/")
    public ResponseEntity deleteGroup(@RequestBody RequestGroupMemberDTO request){
        groupMemberService.deleteGroupMember(request);
        ResponseGroupDto responseGroupDto = groupService.deleteGroup(request.getGroupId());

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }
    //layer를 넘어갈 때는 dto를 받고 같은 계층에서는 각 자료형을 받고 싶은데 요거또 고민을 좀 해야봐야 할것같다.
    //cascade 해놓으면 아마 연관관계 맺어진 데이터도 다 삭제 될거 같긴해요
    //일단은 기능은 완성.. 테스트 해봐야함

    @Operation(
        summary = "get group list API",
        description = "멤버가 소유한 그룹들의 id, 이름 반환 (그룹관리)",
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
    @GetMapping("/info/")
    public ResponseEntity findGroupNames(@RequestParam Long memberId) {
        List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupsInfo(memberId);
//        List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupsInfo(findGroupDto);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    //완


    //친구를 나의 기본 그룹에 추가하는 코드
    @Operation(
            summary = "add friends to defualt group API",
            description = "새로운 친구를 기본그룹에 추가 요청",
            parameters = {
                    @Parameter(name = "memberId", description = "친구를 추가할 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "친구 일괄 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @PostMapping("/groupmember/")
    public ResponseEntity createGroupMember(@RequestBody CreateGroupMemberDTO createGroupMemberDTO){
        final ResponseGroupMemberDTO dto = groupMemberService.createGroupMember(createGroupMemberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
    @GetMapping("/groupmember/notingroup/")
    public ResponseEntity<List<ResponseGroupMemberDTO>> findMemberListNotInGroup(FindGroupMemberDto request) {
        // TODO GroupMember
        /* 친구(기본그룹) 중 groupId 그룹에 포함되지 않은 친구 intraName List 반환  */
        List<ResponseGroupMemberDTO> groupMemberDTOS = groupMemberService.findMemberNotInGroup(request);
//        List<ResponseGroupMemberListDTO> groupmembers = groupMemberService.findGroupMembersbyMemberId(request.getMemberId());
        //기본 그룹 멤버 리스트를 쫙 받는 서비스 코드가 있어야 할 것 같음.
        return ResponseEntity.status(HttpStatus.OK).body(groupMemberDTOS);
    }

    @Operation(
        summary = "add friends to group API",
        description = "친구 리스트를 받아서 해당 그룹에 일괄 추가",
        parameters = {
            @Parameter(name = "groupId", description = "친구를 추가할 그룹 ID", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "친구 일괄 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
              @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
              @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
        }
    )
    @PostMapping("/groupmember-add")
    public ResponseEntity addFriendsToGroup(@RequestBody AddGroupMemberListDTO request) {
        List<ResponseGroupMemberDTO> response = groupMemberService.addFriendsList(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    @GetMapping("/groupmember/")
    public ResponseEntity findIncludeGroupFriendNames(@RequestParam Long groupId) {
        List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupMemberbyGroupId(groupId);
        //intraName, id만 반환하면 될거같은데 쓸데없는 내용까지 같이 있어서 어떻게 해야하나 고민중

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
        summary = "delete group friend API",
        description = "친구 리스트를 받아서 해당 그룹에서 일괄 삭제함. 이때, 친구 리스트는 모두 해당 그룹에 속해있어야 함",
        parameters = {
            @Parameter(name = "groupId", description = "그룹 id", required = true, schema = @Schema(type = "Long"), in = ParameterIn.PATH),
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "삭제하려는 친구 이름 리스트", required = true, content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
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
