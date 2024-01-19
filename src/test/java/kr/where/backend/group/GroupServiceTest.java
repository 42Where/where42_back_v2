package kr.where.backend.group;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.dto.group.UpdateGroupDTO;

import kr.where.backend.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    @Autowired
    private GroupService groupService;
    private CreateGroupDTO createGroupDto;
    @Autowired
    private MemberService memberService;

    private AuthUserInfo authUser;
    @BeforeEach
    public void setUp () {
        // Given
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUserInfo(10000, "phan", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = CadetPrivacy.create(10000, "phan", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupDto = new CreateGroupDTO("popopop");
    }

    @DisplayName("그룹 생성 성공")
    @Test
    @Rollback
    public void testCreateGroup() {
        // When
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto, authUser);

        // Then
        assertNotNull(responseGroupDto.getGroupId());
    }

    @DisplayName("그룹 이름 찾기 성공")
    @Test
    @Rollback
    public void testFindGroupName() {

        //give
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto, authUser);

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
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto, authUser);
        UpdateGroupDTO dto = new UpdateGroupDTO(responseGroupDto.getGroupId(), "group111");

        // When
        ResponseGroupDTO updateDto = groupService.updateGroup(dto, authUser);

        // Then
        assertEquals("group111", updateDto.getGroupName());
    }

    @DisplayName("그룹 이름 삭제 성공")
    @Test
    @Rollback
    public void testDeleteGroup() {
        //give
        ResponseGroupDTO responseGroupDto = groupService.createGroup(createGroupDto, authUser);
        // When
        ResponseGroupDTO deleteDto = groupService.deleteGroup(responseGroupDto.getGroupId(), authUser);
        // Then
        assertEquals("popopop", deleteDto.getGroupName());
    }
}
