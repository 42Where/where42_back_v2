package kr.where.backend.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@Rollback
//public class GroupServiceTest {
//
//    @Autowired
//    private GroupService groupService;
//
//    private CreateGroupDto createGroupDto;
//    private ResponseGroupDto responseGroupDto;
//
//    @BeforeEach
//    public void setUp () {
//        // Given
//        createGroupDto = new CreateGroupDto(10000L, "group");
//        responseGroupDto = groupService.createGroup(createGroupDto);
//    }
//
//    @Test
//    @DisplayName("그룹 생성 성공")
//    public void testCreateGroup() {
//        // When
//        ResponseGroupDto responseGroupDto = groupService.createGroup(createGroupDto);
//
//        // Then
//        assertNotNull(responseGroupDto.getGroupId());
//    }
//
//    @Test
//    @DisplayName("그룹 이름 찾기 성공")
//    public void testFindGroupName() {
//        // When
//        String name = groupService.findGroupName(responseGroupDto.getGroupId());
//
//        // Then
//        assertEquals("group", name);
//    }
//
//    @Test
//    @DisplayName("그룹 이름 업데이트 성공")
//    public void testUpdateGroup() {
//        // Given
//        UpdateGroupDto dto = new UpdateGroupDto(responseGroupDto.getGroupId(), "group111");
//
//        // When
//        ResponseGroupDto updateDto = groupService.updateGroup(dto);
//
//        // Then
//        assertEquals("group111", updateDto.getGroupName());
//    }
//
//    @Test
//    @DisplayName("그룹 이름 삭제 성공")
//    public void testDeleteGroup() {
//        // When
//        ResponseGroupDto deleteDto = groupService.deleteGroup(responseGroupDto.getGroupId());
//
//        // Then
//        assertEquals("group", deleteDto.getGroupName());
//    }
//}
