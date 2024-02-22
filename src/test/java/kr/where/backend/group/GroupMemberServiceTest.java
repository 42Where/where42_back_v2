package kr.where.backend.group;

import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.api.json.Hane;
import kr.where.backend.auth.authUser.AuthUser;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
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

    private AuthUser authUser;
    @BeforeEach
    public void setUp () {
//         Given
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        authUser = new AuthUser(11111, "hjeong", 1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, "", authorities));
        CadetPrivacy cadetPrivacy = new CadetPrivacy(11111, "hjeong", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.create("IN");
        Member member = memberService.createAgreeMember(cadetPrivacy, hane);
        authUser.setDefaultGroupId(member.getDefaultGroupId());
        defaultResponseGroupDTO = groupService.createGroup(new CreateGroupDTO(Group.DEFAULT_GROUP), authUser);
        generalResponseGroupDTO = groupService.createGroup(new CreateGroupDTO("test_group"), authUser);
    }
    @DisplayName("그룹 멤버 생성")
    @Test
    @Rollback
    public void 그룹_멤버_생성() throws Exception {
        //given
        CadetPrivacy cadetPrivacy = new CadetPrivacy(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.create("IN");
        authUser.setIntraId(22222);
        authUser.setIntraName("jnam");
        memberService.createAgreeMember(cadetPrivacy, hane);

        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .intraId(22222)
                .groupId(generalResponseGroupDTO.getGroupId())
                .build();
        //when
        authUser.setIntraId(11111);
        authUser.setIntraName("hjeong");
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false, authUser);

        //then
        assertNotNull(responseGroupMemberDTO.getGroupId());
    }

    void 그룹_멤버_리스트_생성_후_저장(){
        CadetPrivacy cadetPrivacy1 = new CadetPrivacy(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        authUser.setIntraId(22222);
        authUser.setIntraName("jnam");
        Hane hane1 = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy1, hane1);

        CadetPrivacy cadetPrivacy2 = new CadetPrivacy(22223, "suhwpark", "c1r1s1", "image", true, "2022-10-31");
        authUser.setIntraId(22223);
        authUser.setIntraName("suhwpark");
        Hane hane2 = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy2, hane2);

        CadetPrivacy cadetPrivacy3 = new CadetPrivacy(22224, "jonhan", "c1r1s1", "image", true, "2022-10-31");
        authUser.setIntraId(22224);
        authUser.setIntraName("jonhan");
        Hane hane3 = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy3, hane3);

        List<Integer> members = new ArrayList<>();
        members.add(22222);
        members.add(22223);
        members.add(22224);
        authUser.setIntraId(11111);
        authUser.setIntraName("hjeong");
        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(generalResponseGroupDTO.getGroupId())
                .members(members)
                .build();
        List<ResponseOneGroupMemberDTO> add = groupMemberService.addFriendsList(addGroupMemberListDTO, authUser);
        System.out.println("size : " + add.size());
    }

    @DisplayName("그룹 멤버 조회")
    @Test
    public void 그룹_멤버_조회() throws Exception{

        //given
        그룹_멤버_리스트_생성_후_저장();

        //when
        List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS = groupMemberService.findGroupMemberByGroupId(
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
        CadetPrivacy cadetPrivacy = new CadetPrivacy(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        authUser.setIntraId(22222);
        authUser.setIntraName("jnam");
        Hane hane = Hane.create("IN");
        memberService.createAgreeMember(cadetPrivacy, hane);
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .intraId(22222)
                .groupId(generalResponseGroupDTO.getGroupId())
                .build();
        authUser.setIntraId(11111);
        authUser.setIntraName("hjeong");
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false, authUser);

        List<Integer> members = new ArrayList<>();
        members.add(22222);

        //when
        List<ResponseGroupMemberDTO> responseGroupMemberDTO1 = groupMemberService.deleteFriendsList(
                DeleteGroupMemberListDTO.builder()
                .groupId(responseGroupMemberDTO.getGroupId())
                .members(members).build() , authUser);
        //then
        assertEquals(1, responseGroupMemberDTO1.size());
    }

    @DisplayName("기본 그룹의 멤버 삭제")
    @Test
    @Rollback
    public void 기본_그룹_멤버_삭제() throws Exception{

        //given
        //멤버 한명 생성
        CadetPrivacy cadetPrivacy = new CadetPrivacy(22222, "jnam", "c1r1s1", "image", true, "2022-10-31");
        Hane hane = Hane.create("IN");
        authUser.setIntraId(22222);
        authUser.setIntraName("jnam");
        memberService.createAgreeMember(cadetPrivacy, hane);

        //클라이언트의 아이디 다시 세팅
        authUser.setIntraId(11111);
        authUser.setIntraName("hjeong");
        authUser.setDefaultGroupId(defaultResponseGroupDTO.getGroupId());

        //기본그룹에 추가
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .intraId(22222)
                .groupId(defaultResponseGroupDTO.getGroupId())
                .build();
        ResponseGroupMemberDTO responseGroupMemberDTO = groupMemberService.createGroupMember(createGroupMemberDTO, false, authUser);

        //일반 그룹에 추가
        createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .intraId(22222)
                .groupId(generalResponseGroupDTO.getGroupId())
                .build();
        groupMemberService.createGroupMember(createGroupMemberDTO, false, authUser);
        List<Integer> members = new ArrayList<>();
        members.add(22222);

        //when
        //기본그룹에서만 22222번 멤버를 삭제
        List<ResponseGroupMemberDTO> delete_member = groupMemberService.deleteFriendsList(DeleteGroupMemberListDTO.builder()
                .groupId(responseGroupMemberDTO.getGroupId())
                .members(members).build(), authUser);

        List<ResponseOneGroupMemberDTO> generalGroupMember = groupMemberService.findGroupMemberByGroupId(
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
        createGroupDto = new CreateGroupDTO("friends List");
        ResponseGroupDTO dto = groupService.createGroup(createGroupDto, authUser);
        List<Integer> member = new ArrayList<>();
        member.add(22222);

        AddGroupMemberListDTO addGroupMemberListDTO = AddGroupMemberListDTO.builder()
                .groupId(dto.getGroupId())
                .members(member)
                .build();
        authUser.setIntraId(11111);
        authUser.setIntraName("hjeong");
        authUser.setDefaultGroupId(generalResponseGroupDTO.getGroupId());
        groupMemberService.addFriendsList(addGroupMemberListDTO, authUser);

        //when

        List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS= groupMemberService.findMemberNotInGroup(
                dto.getGroupId(), authUser);

        //then
        assertEquals(2, responseGroupMemberDTOS.size());
    }
}
