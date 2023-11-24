package kr.where.backend.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.member.dto.CreateMemberDto;
import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    @Autowired
    private GroupService groupService;
    private CreateGroupDto createGroupDto;
    private CreateMemberDto createMemberDto;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setUp () {
        // Given
        createMemberDto = CreateMemberDto.create(10000L, "phan", 0, "url");
        memberService.signUp(createMemberDto);
        createGroupDto = new CreateGroupDto(10000L, "popopop");
    }

    @DisplayName("그룹 생성 성공")
    @Test
    @Rollback
    public void testCreateGroup() {
        // When
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);

        // Then
        assertNotNull(responseGroupDto.getGroupId());
    }

    @DisplayName("그룹 이름 찾기 성공")
    @Test
    @Rollback
    public void testFindGroupName() {

        //give
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);

        // When
        String name = groupService.findGroupNameById(responseGroupDto.getGroupId());

        // Then
        assertEquals("popopop", name);
    }

    @DisplayName("그룹 이름 업데이트 성공")
    @Test
    @Rollback
    public void testUpdateGroup() {
        // Given
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);
        UpdateGroupDto dto = new UpdateGroupDto(responseGroupDto.getGroupId(), "group111");

        // When
        ResponseGroupDto updateDto = groupService.updateGroup(dto);

        // Then
        assertEquals("group111", updateDto.getGroupName());
    }

    @DisplayName("그룹 이름 삭제 성공")
    @Test
    @Rollback
    public void testDeleteGroup() {
        //give
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);
        // When
        ResponseGroupDto deleteDto = groupService.deleteGroup(responseGroupDto.getGroupId());
        // Then
        assertEquals("popopop", deleteDto.getGroupName());
    }
}
