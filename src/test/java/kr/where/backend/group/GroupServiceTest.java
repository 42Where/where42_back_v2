package kr.where.backend.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityNotFoundException;
import kr.where.backend.group.dto.CreateGroupDto;
import kr.where.backend.group.dto.ResponseGroupDto;
import kr.where.backend.group.dto.UpdateGroupDto;
import kr.where.backend.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    private CreateGroupDto createGroupDto;

    @BeforeEach
    public void setUp () {
        createGroupDto = new CreateGroupDto(10000L, "group");
    }

    @Test
    @DisplayName("그룹 생성 성공")
    public void testCreateGroup() {
        // When
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);

        // Then
        assertNotNull(responseGroupDto.getGroupId());
    }

    @Test
    @DisplayName("그룹 이름 찾기 성공")
    public void testFindGroupName() {
        // Given
        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);

        // When
        String name = groupService.findGroupName(responseGroupDto.getGroupId());

        // Then
        assertEquals("group", name);
    }

    @Test
    @DisplayName("그룹 이름 업데이트 성공")
    public void testUpdateGroup() {
        // Given
        ResponseGroupDto responseGroupDto1 = groupService.createGroup(createGroupDto);

        UpdateGroupDto dto = new UpdateGroupDto(responseGroupDto1.getGroupId(), "group111");

        // When
        ResponseGroupDto responseGroupDto2 = groupService.updateGroup(dto);

        // Then
        assertEquals("group111", responseGroupDto2.getGroupName());
    }

    @Test
    @DisplayName("그룹 이름 삭제 성공")
    public void testDeleteGroup() {
        // Given
        ResponseGroupDto responseGroupDto1 = groupService.createGroup(createGroupDto);

        // When
        ResponseGroupDto responseGroupDto2 = groupService.deleteGroup(responseGroupDto1.getGroupId());

        // Then
        assertEquals("group", responseGroupDto2.getGroupName());
    }
}
