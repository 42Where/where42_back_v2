package kr.where.backend.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.dto.group.UpdateGroupDTO;

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
    private CreateGroupDTO createGroupDto;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setUp () {
        // Given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(10000, "phan", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);

        createGroupDto = new CreateGroupDTO(10000, "popopop");
    }

    @DisplayName("그룹 생성 성공")
    @Test
    @Rollback
    public void testCreateGroup() {
        // When
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto);

        // Then
        assertNotNull(responseGroupDto.getGroupId());
    }

    @DisplayName("그룹 이름 찾기 성공")
    @Test
    @Rollback
    public void testFindGroupName() {

        //give
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto);

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
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto);
        UpdateGroupDTO dto = new UpdateGroupDTO(responseGroupDto.getGroupId(), "group111");

        // When
        ResponseGroupDTO updateDto = groupService.updateGroup(dto);

        // Then
        assertEquals("group111", updateDto.getGroupName());
    }

    @DisplayName("그룹 이름 삭제 성공")
    @Test
    @Rollback
    public void testDeleteGroup() {
        //give
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto);
        // When
        ResponseGroupDTO deleteDto = groupService.deleteGroup(responseGroupDto.getGroupId());
        // Then
        assertEquals("popopop", deleteDto.getGroupName());
    }
}
