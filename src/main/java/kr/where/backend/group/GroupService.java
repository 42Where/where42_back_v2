package kr.where.backend.group;

//import static kr.where.backend.suhwparkException.ErrorCode.DUPLICATE_GROUP_NAME;
//import static kr.where.backend.suhwparkException.ErrorCode.NOT_FOUND_GROUP;

import java.util.List;

//import kr.where.backend.suhwparkException.CustomException;
//import kr.where.backend.suhwparkException.ErrorCode;
import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.group.FindGroupDto;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.group.entity.Group;
import jakarta.persistence.EntityNotFoundException;
import kr.where.backend.group.exception.GroupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;

    @Transactional
    public ResponseGroupDto createGroup(final CreateGroupDto dto){
        validateGroupName(dto);
        Group group = new Group(dto.getGroupName());
        groupRepository.save(group);
        return ResponseGroupDto.from(group);
    }

    private void validateGroupName(final CreateGroupDto dto) {
        RequestGroupMemberDTO requestGroupMemberDTO = RequestGroupMemberDTO.builder().memberId(dto.getMemberIntraId()).build();
        FindGroupDto groupDto = FindGroupDto.builder().memberId(dto.getMemberIntraId()).build();
        List<ResponseGroupMemberDTO> groupIds = groupMemberService.findGroupId(groupDto.getMemberId());
        groupIds.stream().forEach(c -> System.out.println(c));
        groupIds.stream()
                .filter(id -> findGroupName(id.getGroupId()).equals(dto.getGroupName()))
                .findFirst()
                .ifPresent(name -> {
                    throw new GroupException.DuplicatedGroupNameException();
                });
    }

    /* group 이 존재 하는지 유효성 검사 */
    public Group findById(final Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        return group;
    }

    public final String findGroupName(final Long groupId){
        Group group = findById(groupId);
        return group.getGroupName();
    }

    @Transactional
    public ResponseGroupDto updateGroup(final UpdateGroupDto dto) {
        Group group = findById(dto.getGroupId());
        group.setGroupName(dto.getGroupName());
        return ResponseGroupDto.from(group);
    }

    @Transactional
    public ResponseGroupDto deleteGroup(final Long groupId){
        Group group = findById(groupId);
        groupRepository.delete(group);
        return ResponseGroupDto.from(group);
    }

}
