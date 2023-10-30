package kr.where.backend.group;

import kr.where.backend.group.dto.GroupUpdateRequestDTO;
import kr.where.backend.group.entity.Group;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional
    public Long createGroup(String groupName){
        Group group = new Group();
        group.setName(groupName);

        groupRepository.save(group);
        return group.getGroupId();
    }

    /* group 이 존재 하는지 유효성 검사 */
    public Group findById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        return group;
    }

    public String findGroupName(Long groupId){
        Group group = findById(groupId);
        return group.getName();
    }

    @Transactional
    public void updateGroup(GroupUpdateRequestDTO dto) {
        Group group = findById(dto.getGroupId());
        group.setName(dto.getGroupName());
    }

    @Transactional
    public void deleteGroup(Long groupId){
        Group group = findById(groupId);
        groupRepository.delete(group);
    }
}
