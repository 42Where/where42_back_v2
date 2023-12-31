package kr.where.backend.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
            summary = "2.1 create new group API",
            description = "그룹 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "그룹 생성 요청",
                    required = true, content = @Content(schema = @Schema(implementation = CreateGroupDto.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "그룹 생성 성공", content = @Content(schema = @Schema(implementation = ResponseGroupDto.class))),
                    @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example3", value = "{\"statusCode\": 2001, \"responseMsg\": \"그룹 이름 중복\"}"),})),
            }
    )
    @PostMapping("")
    public ResponseEntity createGroup(@RequestBody CreateGroupDto request) {
        final ResponseGroupDto dto = groupService.createGroup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    //완성

    @Operation(
            summary = "2.2 그룹 목록 및 친구 API 조회",
            description = "멤버가 만든 그룹 및 그룹 내의 친구 목록을 조회합니다 (메인 화면 용)",
            parameters = {
                    @Parameter(name = "intraId", description = "intra ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseGroupMemberListDTO.class))),
            }
    )
    @GetMapping("")
    public ResponseEntity findAllGroups(@RequestParam("intraId") Integer intraId) {
        final List<ResponseGroupMemberListDTO> dto = groupMemberService.findMyAllGroupInformation(intraId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
            summary = "2.3 modify group name API",
            description = "그룹 이름 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "변경할 새로운 이름과 아이디", required = true, content = @Content(schema = @Schema(implementation = UpdateGroupDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹 이름 변경 성공", content = @Content(schema = @Schema(implementation = UpdateGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"존재하지 않는 그룹\"}"),})),
                    @ApiResponse(responseCode = "409", description = "그룹 이름 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 409, \"responseMsg\": \"그룹 이름 중복\"}"),})),
            }
    )
    @PostMapping("/name")
    public ResponseEntity updateGroupName(@RequestBody @Valid UpdateGroupDto dto) {
        final ResponseGroupDto responseGroupDto = groupService.updateGroup(dto);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @Operation(
            summary = "2.4 delete group API",
            description = "그룹 삭제",
            parameters = {
                    @Parameter(name = "groupId", description = "삭제할 그룹 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹 삭제 성공\", \"data\": [ {\"groupId\": 3} ] }"),})),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @DeleteMapping("")
    public ResponseEntity deleteGroup(@RequestParam Long groupId) {
        final ResponseGroupDto responseGroupDto = groupService.deleteGroup(groupId);

        return ResponseEntity.status(HttpStatus.OK).body(responseGroupDto);
    }

    @Operation(
            summary = "2.5 get group list API",
            description = "멤버가 소유한 그룹들의 id, 이름 반환 (그룹관리)",
            parameters = {
                    @Parameter(name = "intraId", description = "나의 intraId", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseGroupMemberDTO.class))),
                    @ApiResponse(responseCode = "401", description = "등록되지 않은 카뎃", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"등록되지 않은 카뎃\"}"),})),
            }
    )
    @GetMapping("/info")
    public ResponseEntity findGroupNames(@RequestParam("intraId") Integer intraId) {
        final List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupsInfoByIntraId(intraId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
            summary = "2.6 add friends to defualt group API",
            description = "새로운 친구를 기본그룹에 추가 요청(멤버의 기본그룹 ID와, 추가할 멤버 ID)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가하려는 친구", required = true, content = @Content(schema = @Schema(implementation = CreateGroupMemberDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "친구 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
                    @ApiResponse(responseCode = "401", description = "존재하지 않는 멤버", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 401, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @PostMapping("/groupmember")
    public ResponseEntity createGroupMember(@RequestBody CreateGroupMemberDTO createGroupMemberDTO) {
        final ResponseGroupMemberDTO dto = groupMemberService.createGroupMember(createGroupMemberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "2.7 get not included friends in group API",
            description = "그룹에 포함되지 않은 친구 목록을 조회. 그룹에 기본 그룹의 친구를 추가하기 위함이다. 이때, 조회되는 친구들은 멤버가 친구로 등록하되 해당 그룹에 등록되지 않은 친구들이다.",
            parameters = {
                    @Parameter(name = "default groupId", description = "나의 기본그룹 id", required = true, in = ParameterIn.QUERY),
                    @Parameter(name = "groupId", description = "해당 그룹 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class))),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"존재하지 않는 그룹\"}")})),
            }
    )
    @PostMapping("/groupmember/not-ingroup")
    public ResponseEntity<List<ResponseGroupMemberDTO>> findMemberListNotInGroup(@RequestParam("default groupId") Long default_groupID, @RequestParam("groupId") Long groupId) {
        final List<ResponseGroupMemberDTO> groupMemberDTOS = groupMemberService.findMemberNotInGroup(default_groupID, groupId);

        return ResponseEntity.status(HttpStatus.OK).body(groupMemberDTOS);
    }

    @Operation(
            summary = "2.8 add friends to group API",
            description = "친구 리스트를 받아서 해당 그룹에 일괄 추가",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가하려는 친구 ID 리스트와 친구를 추가할 그룹 ID", required = true, content = @Content(schema = @Schema(implementation = AddGroupMemberListDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "친구 일괄 추가 성공", content = @Content(schema = @Schema(implementation = ResponseWithData.class), examples = {
                            @ExampleObject(name = "example1", value = "{ \"statusCode\": 201, \"responseMsg\": \"그룹에 친구 추가 성공\"}"),})),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @PostMapping("/groupmember/members")
    public ResponseEntity addFriendsToGroup(@RequestBody AddGroupMemberListDTO request) {
        final List<ResponseGroupMemberDTO> response = groupMemberService.addFriendsList(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "2.9 get group friend list API",
            description = "그룹 내의 모든 친구 목록 조회",
            parameters = {
                    @Parameter(name = "groupId", description = "조회를 원하는 그룹 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseGroupMemberDTO.class))),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @GetMapping("/groupmember")
    public ResponseEntity findAllGroupFriends(@RequestParam("groupId") Long groupId) {
        final List<ResponseGroupMemberDTO> dto = groupMemberService.findGroupMemberbyGroupId(groupId);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "2.10 delete group friend API",
            description = "친구 리스트를 받아서 해당 그룹에서 일괄 삭제함. 이때, 친구 리스트는 모두 해당 그룹에 속해있어야 함",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "삭제하려는 친구 id 리스트", required = true, content = @Content(schema = @Schema(implementation = DeleteGroupMemberListDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ResponseGroupMemberDTO.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 200, \"responseMsg\": \"그룹에서 친구 삭제 성공\"}"),})),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 그룹", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "example1", value = "{\"statusCode\": 400, \"responseMsg\": \"데이터를 찾을 수 없음\"}"),})),
            }
    )
    @PutMapping ("/groupmember")
    public ResponseEntity removeIncludeGroupFriends(@RequestBody DeleteGroupMemberListDto request) {
        final List<ResponseGroupMemberDTO> ResponseGroupMemberDTOs = groupMemberService.deleteFriendsList(request);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseGroupMemberDTOs);
    }
}
