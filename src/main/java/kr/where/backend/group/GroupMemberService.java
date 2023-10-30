package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.where.backend.group.dto.GroupCreateRequestDTO;
import kr.where.backend.group.dto.GroupMemberResponseDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupService groupService;
    private final GroupRepository groupRepository;

    @Transactional
    public void createGroupMember(GroupCreateRequestDTO requestDTO, Long groupId, boolean isOwner){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setMember(member);
        groupMember.setIsOwner(isOwner);

        groupMemberRepository.save(groupMember);
    }

    public List<Long> findGroupId(Long memberId){
        List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndIsOwner(memberId, true);
        List<Long> collect = groupMembers.stream()
                .map(m -> m.getGroup().getGroupId())
                .collect(Collectors.toList());
        return collect;
    }

    public List<Long> findGroupMemberId(Long groupId){
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(groupId);
        List<Long> collect = groupMembers.stream()
                .map(m -> m.getMember().getId())
                .collect(Collectors.toList());
        return collect;
    }

    @Transactional
    public void deleteGroupMember(Long groupId){
        groupMemberRepository.deleteAllByGroup_GroupId(groupId);
    }

    public List<GroupMemberResponseDTO> findGroupMembers(Long memberId){
        List<Long> groups = findGroupId(memberId);
        List<GroupMemberResponseDTO> dtoList = new ArrayList<>();
        for (Long g :groups){
            GroupMemberResponseDTO dto = new GroupMemberResponseDTO();
            dto.setGroupId(g);
            dto.setGroupName(groupService.findGroupName(g));
            List<Long> friends = findGroupMemberId(g);
            dto.setCount(friends.size());
            dto.setMembers(friends);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
