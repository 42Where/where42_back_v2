package kr.where.backend.group;

import java.util.List;

import kr.where.backend.group.dto.group.CreateGroupDto;
import kr.where.backend.group.dto.groupmember.RequestGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDto;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.group.UpdateGroupDto;
import kr.where.backend.group.entity.Group;
import jakarta.persistence.EntityNotFoundException;
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
        List<ResponseGroupMemberDTO> groupIds = groupMemberService.findGroupId(requestGroupMemberDTO);
        groupIds.stream().forEach(c -> System.out.println(c));
        if (groupIds.stream()
                .filter(id -> findGroupName(id.getGroupId()).equals(dto.getGroupName()))
                .count() != 0)
            throw new RuntimeException("그룹 이름 중복");
    }

    /* group 이 존재 하는지 유효성 검사 */
    public Group findById(final Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
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
