package kr.where.backend.group;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.member.Member;
import kr.where.backend.member.dto.ResponseMemberDTO;
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

    private CreateGroupDTO createGroupDto;
    private ResponseMemberDTO responseMemberDto;
    private ResponseGroupDTO responseGroupDto;
    private CreateGroupMemberDTO createGroupMemberDTO;

    private ResponseGroupDTO defaultResponseGroupDTO;
    private ResponseGroupDTO generalResponseGroupDTO;


    @BeforeEach
    public void setUp () {
//         Given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(11111, "hjeong", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);

        defaultResponseGroupDTO = groupService.createGroup(new CreateGroupDTO(member.getIntraId(), Group.DEFAULT_GROUP));
        member.setDefaultGroupId(defaultResponseGroupDTO.getGroupId());

        generalResponseGroupDTO = groupService.createGroup(new CreateGroupDTO(11111, "test_group"));
    }
    @DisplayName("그룹 멤버 생성")
    @Test
    @Rollback
    public void 그룹_멤버_생성() throws Exception {
        //given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(generalResponseGroupDTO.getGroupId())
                .intraId(cadetPrivacy.getId())
                .build();
        //when
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false);

        //then
        assertNotNull(responseGroupMemberDTO.getGroupId());
    }

    void 그룹_멤버_리스트_생성_후_저장(){
        CadetPrivacy cadetPrivacy1 = CadetPrivacy.createForTest(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane1 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy1, hane1);

        CadetPrivacy cadetPrivacy2 = CadetPrivacy.createForTest(22223, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
        Hane hane2 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy2, hane2);

        CadetPrivacy cadetPrivacy3 = CadetPrivacy.createForTest(22224, "jonhan", "c1r1s1", "image", true, "2022-10-31");
        Hane hane3 = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy3, hane3);

        List<String> members = new ArrayList<>();
        members.add("jnam");
        members.add("suhwpark");
        members.add("jonhan");
        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(generalResponseGroupDTO.getGroupId())
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
        List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS = groupMemberService.findGroupMemberbyGroupId(
                generalResponseGroupDTO.getGroupId());

        //then
        for (ResponseOneGroupMemberDTO memberDTO : responseGroupMemberDTOS) {
            System.out.println(memberDTO);
        }
        assertEquals(3, responseGroupMemberDTOS.size());
    }

    @DisplayName("그룹 멤버 삭제")
    @Test
    @Rollback
    public void 그룹_멤버_삭제() throws Exception{

        //given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(generalResponseGroupDTO.getGroupId())
                .intraId(cadetPrivacy.getId())
                .build();
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false);

        List<Integer> members = new ArrayList<>();
        members.add(22222);

        //when
        List<ResponseGroupMemberDTO> responseGroupMemberDTO1 = groupMemberService.deleteFriendsList(
                DeleteGroupMemberListDTO.builder()
                .groupId(responseGroupMemberDTO.getGroupId())
                .members(members).build());
        //then
        assertEquals(1, responseGroupMemberDTO1.size());
    }

    @DisplayName("기본 그룹의 멤버 삭제")
    @Test
    @Rollback
    public void 기본_그룹_멤버_삭제() throws Exception{

        //given
        CadetPrivacy cadetPrivacy = CadetPrivacy.createForTest(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.createForTest("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(defaultResponseGroupDTO.getGroupId())
                .intraId(cadetPrivacy.getId())
                .build();
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false);

        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(generalResponseGroupDTO.getGroupId())
                .intraId(cadetPrivacy.getId())
                .build();
        groupMemberService.createGroupMember(createGroupMemberDTO, false);
        //기본그룹과, 일반 그룹에 22222번 멤버를 동시에 추가
        List<Integer> members = new ArrayList<>();
        members.add(22222);

        //when
        //기본그룹에서만 22222번 멤버를 삭제
        List<ResponseGroupMemberDTO> responseGroupMemberDTO1 = groupMemberService.deleteFriendsList(
                DeleteGroupMemberListDTO.builder()
                .groupId(responseGroupMemberDTO.getGroupId())
                .members(members).build());

        List<ResponseOneGroupMemberDTO> generalGroupMember = groupMemberService.findGroupMemberbyGroupId(
                generalResponseGroupDTO.getGroupId());
        //then

        //기본그룹에서 삭제 될 경우 일반멤버의 멤버수도 0인걸 확인 가능
        assertEquals(0, generalGroupMember.size());
    }


    @DisplayName("기본그룹_제외_그룹멤버_조회")
    @Test
    public void 기본그룹_제외_그룹멤버_조회() throws Exception{

        //given 기본그룹이 아닌 그룹 하나 새로 만든 후 멤버 한명 추가
        그룹_멤버_리스트_생성_후_저장();
        createGroupDto = new CreateGroupDTO(11111, "friends List");
        ResponseGroupDTO dto = groupService.createGroup(createGroupDto);
        List<String> member = new ArrayList<>();
        member.add("jnam");
        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(dto.getGroupId())
                .members(member)
                .build();
        groupMemberService.addFriendsList(addGroupMemberListDTO);

        //when
        List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS= groupMemberService.findMemberNotInGroup(
                generalResponseGroupDTO.getGroupId(), dto.getGroupId());

        //then
        assertEquals(2, responseGroupMemberDTOS.size());
        //그룹의 오너는 항상 모든 그룹에 포함되어 있어서 매번 중복되어 없어짐
    }
}
