package kr.where.backend.groupMemberServiceTest;

import kr.where.backend.group.GroupMemberRepository;
import kr.where.backend.group.GroupMemberService;
import kr.where.backend.group.GroupRepository;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.dto.GroupCreateRequestDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.Enum.MemberLevel;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static kr.where.backend.member.Enum.MemberLevel.administrator;
import static kr.where.backend.member.Enum.MemberLevel.member;

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

    private MemberRepository memberRepository;


    @Test
    public void FindGroupMemberTest(){

        //given
        Long id = groupService.createGroup("test Group");
        CreateMemberDto createMemberDto = CreateMemberDto.create(11111L, "jonhan", 1, "img");
        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
        GroupCreateRequestDTO dto = new GroupCreateRequestDTO();
        dto.setMemberId(responseMemberDto.getIntraId());
        dto.setIntraId(responseMemberDto.getIntraId());
        dto.setGroupName("test Group");
        System.out.println("저장한 id :" + responseMemberDto.getIntraId());
        System.out.println("찾아온 id :" + memberService.findAll().get(0).getIntraId().toString());

        //when
        groupMemberService.createGroupMember(dto, id, true);
        List<Long> list = groupMemberService.findGroupId(1L);

        //then
        assertEquals(list.get(0).toString(), "1");

        //미쳣고 바로 만들어 버렸고 jonhan 백엔드에 소질 있나? 내가 볼땐 전혀 아니고
    }

    @Test
    public void deleteGroupMemberTest(){

    }
}
