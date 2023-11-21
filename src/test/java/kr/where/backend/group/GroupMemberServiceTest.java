package kr.where.backend.group;

import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.FindGroupDto;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
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

//@RequiredArgsConstructor
//@RunWith(SpringRunner.class)
//@Transactional
//@SpringBootTest
//@Slf4j
//public class GroupMemberServiceTest {
//
//    @Autowired
//    private GroupMemberService groupMemberService;
//    @Autowired
//    private GroupMemberRepository groupMemberRepository;
//    @Autowired
//    private GroupRepository groupRepository;
//    @Autowired
//    private GroupService groupService;
//    @Autowired
//    private MemberService memberService;
//    private MemberRepository memberRepository;
//
//    CreateMemberDto createMemberDto = CreateMemberDto.create(11111L, "jonhan", 1, "img");
//    CreateGroupDto createGroupDto = new CreateGroupDto(11111L,"test Group");
//
//    @Test
//    @DisplayName("그룹 멤버 생성")
//    public void createGroupMemberTest() throws Exception{
//        //given
//        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
//        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
//        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
//                .intraId(responseMemberDto.getIntraId())
//                .groupId(groupDto.getGroupId())
//                .groupName("test Group")
//                .isOwner(true)
//                .build();
//        //when
//        ResponseGroupMemberDTO ResponseDto = groupMemberService.createGroupMember(dto);
//        //then
//        assertEquals(ResponseDto.getMemberId().toString(), "1");
//    }
//
//    @Test
//    @DisplayName("그룹에 저장된 멤버 찾기")
//    public void findGroupMemberTest(){
//
//        //given
//        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
//        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
//        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
//                .intraId(responseMemberDto.getIntraId())
//                .groupId(groupDto.getGroupId())
//                .groupName("test Group")
//                .isOwner(true)
//                .build();
//        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().memberId(responseMemberDto.getIntraId())
//                        .groupId(groupDto.getGroupId()).build();
//        System.out.println("저장한 id :" + responseMemberDto.getIntraId());
//        System.out.println("찾아온 id :" + memberService.findAll().get(0).getIntraId().toString());
//        FindGroupDto groupDto1 = FindGroupDto.builder().memberId(responseMemberDto.getIntraId()).build();
//
//        //when
//        groupMemberService.createGroupMember(dto);
//        List<ResponseGroupMemberDTO> list = groupMemberService.findGroupId(groupDto1.getMemberId());
//
//        //then
//        assertEquals(list.get(0).getMemberId().toString(), "1");
//    }
//    @Test
//    @DisplayName("그룹 멤버 삭제")
//    public void deleteGroupMemberTest(){
//        //given
//        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
//        ResponseMemberDto responseMemberDto = memberService.createMember(createMemberDto);
//        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder().groupId(groupDto.getGroupId())
//                .groupName("test Group")
//                .intraId(responseMemberDto.getIntraId())
//                        .build();
//        groupMemberService.createGroupMember(createGroupMemberDTO);
//        RequestGroupMemberDTO requestdto = RequestGroupMemberDTO.builder().groupId(groupDto.getGroupId()).memberId(responseMemberDto.getIntraId())
//                .build();
//        //when
//        ResponseGroupMemberDTO responseDto = groupMemberService.deleteGroupMember(requestdto);
//        //then
//        assertEquals(requestdto.getMemberId().toString(), "11111");
//    }
//}
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
    CreateGroupDto createGroupDto = new CreateGroupDto(11111L,"test Group");

    @Test
    @DisplayName("그룹 멤버 생성")
    public void createGroupMemberTest() throws Exception{
        //given
        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
        ResponseMemberDto responseMemberDto = memberService.signUp(createMemberDto);
        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
                .intraId(responseMemberDto.getIntraId())
                .groupId(groupDto.getGroupId())
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
        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
        ResponseMemberDto responseMemberDto = memberService.signUp(createMemberDto);
        CreateGroupMemberDTO dto = CreateGroupMemberDTO.builder()
                .intraId(responseMemberDto.getIntraId())
                .groupId(groupDto.getGroupId())
                .groupName("test Group")
                .isOwner(true)
                .build();
        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().memberId(responseMemberDto.getIntraId())
                        .groupId(groupDto.getGroupId()).build();
        System.out.println("저장한 id :" + responseMemberDto.getIntraId());
        System.out.println("찾아온 id :" + memberService.findAll().get(0).getIntraId().toString());
        FindGroupDto groupDto1 = FindGroupDto.builder().memberId(responseMemberDto.getIntraId()).build();

        //when
        groupMemberService.createGroupMember(dto);
        List<ResponseGroupMemberDTO> list = groupMemberService.findGroupId(groupDto1.getMemberId());

        //then
        assertEquals(list.get(0).getMemberId().toString(), "1");
    }
    @Test
    @DisplayName("그룹 멤버 삭제")
    public void deleteGroupMemberTest(){
        //given
        ResponseGroupDto groupDto = groupService.createGroup(createGroupDto);
        ResponseMemberDto responseMemberDto = memberService.signUp(createMemberDto);
        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder().groupId(groupDto.getGroupId())
                .groupName("test Group")
                .intraId(responseMemberDto.getIntraId())
                        .build();
        groupMemberService.createGroupMember(createGroupMemberDTO);
        RequestGroupMemberDTO requestdto = RequestGroupMemberDTO.builder().groupId(groupDto.getGroupId()).memberId(responseMemberDto.getIntraId())
                .build();
        //when
        ResponseGroupMemberDTO responseDto = groupMemberService.deleteGroupMember(requestdto);
        //then
        assertEquals(requestdto.getMemberId().toString(), "11111");
    }
}
