package kr.where.backend.group;

import kr.where.backend.group.dto.CreateGroupMemberDTO;
import kr.where.backend.group.dto.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.RequestGroupMemberDTO;
import kr.where.backend.member.DTO.CreateMemberDto;
import kr.where.backend.member.DTO.ResponseMemberDto;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

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

    CreateMemberDto createMemberDto = CreateMemberDto.create(11111L, "jonhan", 1, "img");


    @Test
    @DisplayName("그룹 멤버 생성")
    public void createGroupMemberTest() throws Exception{
        //given
        Long id = groupService.createGroup("test Group");
        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
                .intraId(responseMemberDto.getIntraId())
                .groupId(id)
                .groupName("test Group")
                .isOwner(true)
                .build();
        //when
        ResponseGroupMemberDTO ResponseDto = groupMemberService.createGroupMember(dto);
        //then
        assertEquals(ResponseDto.getMemberId().toString(), "1");
    }

    @Test
    @DisplayName("그룹에 저장된 멤버 찾기")
    public void findGroupMemberTest(){

        //given
        Long id = groupService.createGroup("test Group");
        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
                .intraId(responseMemberDto.getIntraId())
                .groupId(id)
                .groupName("test Group")
                .isOwner(true)
                .build();
        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().memberId(responseMemberDto.getIntraId())
                        .groupId(id).build();
        System.out.println("저장한 id :" + responseMemberDto.getIntraId());
        System.out.println("찾아온 id :" + memberService.findAll().get(0).getIntraId().toString());

        //when
        groupMemberService.createGroupMember(dto);
        List<ResponseGroupMemberDTO> list = groupMemberService.findGroupId(requestGroupMemberDTO);

        //then
        assertEquals(list.get(0).getMemberId().toString(), "1");
    }
    @Test
    @DisplayName("그룹 멤버 삭제")
    public void deleteGroupMemberTest(){
        //given
        Long id = groupService.createGroup("test Group");
        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder().groupId(id)
                .groupName("test Group")
                .intraId(responseMemberDto.getIntraId())
                        .build();
        groupMemberService.createGroupMember(createGroupMemberDTO);
        RequestGroupMemberDTO requestdto = RequestGroupMemberDTO.builder().groupId(id).memberId(responseMemberDto.getIntraId())
                .build();
        //when
        ResponseGroupMemberDTO responseDto = groupMemberService.deleteGroupMember(requestdto);
        //then
        assertEquals(requestdto.getMemberId().toString(), "11111");
    }
}
