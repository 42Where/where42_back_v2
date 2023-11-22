package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.FindGroupDto;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@Slf4j
public class GroupMemberServiceTest {

    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    private CreateGroupDto createGroupDto;
    private CreateMemberDto createMemberDto;
    private ResponseMemberDto responseMemberDto;
    private ResponseGroupDto responseGroupDto;
    private CreateGroupMemberDTO createGroupMemberDTO;

    @BeforeEach
    public void setUp () {
//         Given
        createMemberDto = CreateMemberDto.create(11111L, "hjeong", 1, "img");
        createGroupDto = new CreateGroupDto(11111L, "group");
        responseMemberDto = memberService.createMember(createMemberDto);
        responseGroupDto = groupService.createGroup(createGroupDto);
        //        CreateGroupDto createDefaultGroupDto = new CreateGroupDto(11111L,"Default Group");

    }
    @DisplayName("그룹 멤버 생성")
    @Test
    @Rollback
    public void createGroupMemberTest() throws Exception {
        //given
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(responseGroupDto.getGroupId())
                .intraId(responseMemberDto.getIntraId())
                .isOwner(true)
                .build();
        //when
//        assertThatThrownBy(() -> groupMemberService.createGroupMember(createGroupMemberDTO))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("이미 그룹 멤버로 등록된 사용자입니다.");

        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO);
        //then
        assertNotNull(responseGroupMemberDTO.getGroupId());
    }
}
