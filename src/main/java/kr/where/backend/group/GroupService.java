package kr.where.backend.group;

//import static kr.where.backend.exception.ErrorCode.DUPLICATE_GROUP_NAME;
//import static kr.where.backend.exception.ErrorCode.NOT_FOUND_GROUP;

import java.util.List;

//import kr.where.backend.exception.CustomException;
//import kr.where.backend.exception.ErrorCode;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.group.entity.Group;
import jakarta.persistence.EntityNotFoundException;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
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
    public ResponseGroupDto createGroup(final CreateGroupDto dto){
        //그룹을 먼저 만들고, 그룹이 만들어지면 동시에 그 소유주 그룹 멤버는 isOwner가 true인 채로 생성되어야한다.
        validateGroupName(dto);
        Group group = new Group(dto.getGroupName());
        groupRepository.save(group);


        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .groupId(group.getGroupId()).intraId(dto.getIntraId()).build();
        groupMemberService.createGroupMember(createGroupMemberDTO, true);
        return ResponseGroupDto.from(group);
    }

    private void validateGroupName(final CreateGroupDto dto) {
        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().intraId(dto.getIntraId()).build();
        List<ResponseGroupMemberDTO> groupIds = groupMemberService.findGroupIdByIntraId(dto.getIntraId());
        groupIds.stream().forEach(c -> System.out.println(c));
//        if (groupIds.stream()
//                .filter(id -> findGroupNameById(id.getGroupId()).equals(dto.getGroupName()))
//                .count() != 0)
//            throw new GroupException.DuplicatedGroupNameException();
    }

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

    public final void updateGroupValidate(final UpdateGroupDto dto)
    {
        Member member = memberRepository.findByIntraId(dto.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        if (dto.getGroupId() == member.getDefaultGroupId())
            throw new GroupException.CannotModifyGroupException();
        List<ResponseGroupMemberDTO> groups = groupMemberService.findGroupsInfoByIntraId(dto.getIntraId());
        if (!groups.stream().map(ResponseGroupMemberDTO::getGroupId).anyMatch(groupId -> groupId.equals(dto.getGroupId()))) {
            throw new GroupException.CannotModifyGroupException();
        }
    }
    @Transactional
    public ResponseGroupDto updateGroup(final UpdateGroupDto dto){
        updateGroupValidate(dto);
        Group group = findOneGroupById(dto.getGroupId());
        group.setGroupName(dto.getGroupName());
        return ResponseGroupDto.from(group);
    }

    @Transactional
    public ResponseGroupDto deleteGroup(final Long groupId){
        Group group = findOneGroupById(groupId);
        groupRepository.delete(group);
        return ResponseGroupDto.from(group);
    }

}
