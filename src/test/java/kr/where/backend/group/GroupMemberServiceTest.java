package kr.where.backend.group;

import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.api.mappingDto.Hane;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.member.dto.ResponseMemberDto;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private ResponseMemberDto responseMemberDto;
    private ResponseGroupDto responseGroupDto;
    private CreateGroupMemberDTO createGroupMemberDTO;

    @BeforeEach
    public void setUp () {
//         Given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(11111L, "hjeong", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupDto = new CreateGroupDto(11111L, "group");
        responseGroupDto = groupService.createGroup(createGroupDto);
    }
    @DisplayName("그룹 멤버 생성")
    @Test
    @Rollback
    public void 그룹_멤버_생성() throws Exception {
        //given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(22222L, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(responseGroupDto.getGroupId())
                .intraId(cadetPrivacy.getId())
                .isOwner(true)
                .build();
        //when
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO);

        //then
        assertNotNull(responseGroupMemberDTO.getGroupId());
    }

    void 그룹_멤버_리스트_생성_후_저장(){
        CadetPrivacy cadetPrivacy1 = CadetPrivacy.createForTest(22222L, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane1 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy1, hane1);

        CadetPrivacy cadetPrivacy2 = CadetPrivacy.createForTest(22223L, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
        Hane hane2 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy2, hane2);

        CadetPrivacy cadetPrivacy3 = CadetPrivacy.createForTest(22224L, "jonhan", "c1r1s1", "image", true, "2022-10-31");
        Hane hane3 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy3, hane3);

        List<String> members = new ArrayList<>();
        members.add("jnam");
        members.add("suhwpark");
        members.add("jonhan");
        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(responseGroupDto.getGroupId())
                .members(members)
                .build();
        groupMemberService.addFriendsList(addGroupMemberListDTO);
    }

    @DisplayName("그룹 멤버 조회")
    @Test
    public void 그룹_멤버_조회() throws Exception{

        //given
        그룹_멤버_리스트_생성_후_저장();

        //when
        List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMemberService.findGroupMemberbyGroupId(responseGroupDto.getGroupId());

        //then
        for (ResponseGroupMemberDTO memberDTO : responseGroupMemberDTOS) {
            System.out.println(memberDTO);
        }
        assertEquals(4, responseGroupMemberDTOS.size());
    }

    @DisplayName("그룹 멤버 삭제")
    @Test
    @Rollback
    public void 그룹_멤버_삭제() throws Exception{

        //given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(22222L, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(responseGroupDto.getGroupId())
                .intraId(cadetPrivacy.getId())
                .isOwner(true)
                .build();
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO);

        List<Long> members = new ArrayList<>();
        members.add(22222L);
        //when
        List<ResponseGroupMemberDTO> responseGroupMemberDTO1 = groupMemberService.deleteFriendsList(DeleteGroupMemberListDto.builder()
                .groupId(responseGroupMemberDTO.getGroupId())
                .members(members).build());
        //then
        assertEquals(1, responseGroupMemberDTO1.size());
    }


    @DisplayName("기본그룹_제외_그룹멤버_조회")
    @Test
    public void 기본그룹_제외_그룹멤버_조회() throws Exception{

        //given 기본그룹이 아닌 그룹 하나 새로 만든 후 멤버 한명 추가
        그룹_멤버_리스트_생성_후_저장();
        createGroupDto = new CreateGroupDto(11111L, "friends List");
        ResponseGroupDto dto = groupService.createGroup(createGroupDto);
        List<String> member = new ArrayList<>();
        member.add("jnam");
        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(dto.getGroupId())
                .members(member)
                .build();
        groupMemberService.addFriendsList(addGroupMemberListDTO);

        //when
        List<ResponseGroupMemberDTO> responseGroupMemberDTOS= groupMemberService.findMemberNotInGroup(responseGroupDto.getGroupId(), dto.getGroupId());

        //then
        assertEquals(2, responseGroupMemberDTOS.size());
        //그룹의 오너는 항상 모든 그룹에 포함되어 있어서 매번 중복되어 없어짐
    }
}

