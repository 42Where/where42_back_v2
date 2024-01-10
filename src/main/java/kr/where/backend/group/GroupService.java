package kr.where.backend.group;

import java.util.List;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.group.UpdateGroupDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseGroupDTO createGroup(final CreateGroupDTO dto, final AuthUserInfo authUser){
        //그룹을 먼저 만들고, 그룹이 만들어지면 동시에 그 소유주 그룹 멤버는 isOwner가 true인 채로 생성되어야한다.
//        validateGroupName(dto, authUser.getIntraId());
        Group group = new Group(dto.getGroupName());
        groupRepository.save(group);


        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(group.getGroupId()).build();
        groupMemberService.createGroupMember(createGroupMemberDTO, true, authUser);
        return ResponseGroupDTO.from(group);
    }

//    private void validateGroupName(final CreateGroupDTO dto, final Integer intraId) {
//        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().intraId(intraId).build();
//        List<ResponseGroupMemberDTO> groupIds = groupMemberService.findGroupIdByIntraId(intraId);
//        groupIds.stream().forEach(c -> System.out.println(c));
//        if (groupIds.stream()
//                .filter(id -> findGroupNameById(id.getGroupId()).equals(dto.getGroupName()))
//                .count() != 0)
//            throw new GroupException.DuplicatedGroupNameException();
//    }

    /* group 이 존재 하는지 유효성 검사 */
    public Group findOneGroupById(final Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        return group;
    }

    public final String findGroupNameById(final Long groupId){
        Group group = findOneGroupById(groupId);
        return group.getGroupName();
    }

    public final void updateGroupValidate(final UpdateGroupDTO dto, final AuthUserInfo authUser)
    {
//        Member member = memberRepository.findByIntraId(dto.getIntraId())
//                .orElseThrow(MemberException.NoMemberException::new);
        if (dto.getGroupId().equals(authUser.getDefaultGroupId())) {
            throw new GroupException.CannotModifyGroupException();
        }
        List<ResponseGroupMemberDTO> groups = groupMemberService.findGroupsInfoByIntraId(authUser);
        if (groups.stream().map(ResponseGroupMemberDTO::getGroupId).noneMatch(groupId -> groupId.equals(dto.getGroupId()))) {
            throw new GroupException.CannotModifyGroupException();
        }
    }
    @Transactional
    public ResponseGroupDTO updateGroup(final UpdateGroupDTO dto, final AuthUserInfo authUser){
        updateGroupValidate(dto, authUser);
        Group group = findOneGroupById(dto.getGroupId());
        group.setGroupName(dto.getGroupName());
        return ResponseGroupDTO.from(group);
    }

    @Transactional
    public ResponseGroupDTO deleteGroup(final Long groupId, final AuthUserInfo authUser){
        final Group group = findOneGroupById(groupId);
        //validate 추가
        groupRepository.delete(group);

        return ResponseGroupDTO.from(group);
    }

}
