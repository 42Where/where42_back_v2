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
import kr.where.backend.group.dto.GroupCreateRequestDTO;
import kr.where.backend.group.dto.GroupMemberListResponseDTO;
import kr.where.backend.group.dto.GroupUpdateRequestDTO;
import kr.where.backend.utils.response.ResponseWithData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "그룹 이름", required = true, content = @Content(schema = @Schema(type = "string"))),
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
    public ResponseEntity createGroup(@RequestBody @Valid GroupCreateRequestDTO request){
        Long groupId = groupService.createGroup(request.getGroupName());
        groupMemberService.createGroupMember(request, groupId,true);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity findGroup(@RequestParam Long memberId){
        List<GroupMemberListResponseDTO> dto =  groupMemberService.findGroupMembers(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

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
    @PatchMapping("/")
    public ResponseEntity updateGroup(@RequestBody @Valid GroupUpdateRequestDTO dto){
        groupService.updateGroup(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
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
    @DeleteMapping("/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id") Long groupId){
        groupMemberService.deleteGroupMember(groupId);
        groupService.deleteGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
