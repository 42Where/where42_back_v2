package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.group.dto.FindGroupMemberResponseDTO;
import kr.where.backend.group.dto.GroupCreateRequestDTO;
import kr.where.backend.group.dto.GroupMemberListResponseDTO;
import kr.where.backend.group.dto.GroupMemberResponseDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSInput;

@Service
@AllArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupService groupService;
    private final GroupRepository groupRepository;

    @Transactional
    public GroupMemberResponseDTO createGroupMember(final GroupCreateRequestDTO requestDTO, Long groupId, boolean isOwner){
        final Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));

        final GroupMember groupMember = new GroupMember(group, member, isOwner);
        groupMemberRepository.save(groupMember);

        final GroupMemberResponseDTO createDTO = GroupMemberResponseDTO.builder()
                .groupId(groupId)
                .groupName(requestDTO.getGroupName())
                .memberId(member.getId()).build();
        return createDTO;
    }


    public List<GroupMemberResponseDTO> findGroupId(final Long memberId){
        final List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndIsOwner(memberId, true);

        final List<GroupMemberResponseDTO> groupMemberResponseDTOS = groupMembers.stream().map(m ->
            GroupMemberResponseDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getName())
                    .memberId(memberId).build()).toList();

//      List<GroupMemberResponseDTO> groupMemberResponseDTOS = groupMembers.stream().map(groupMember -> {
//            GroupMemberResponseDTO dto = new GroupMemberResponseDTO(groupMember.getGroup().getGroupId(), groupMember.getGroup().getName());
//            return dto;
//        }) .collect(Collectors.toList());
        return groupMemberResponseDTOS;
    }


    public List<FindGroupMemberResponseDTO> findGroupMemberId(final Long groupId){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(groupId);
        final List<FindGroupMemberResponseDTO>  findGroupMemberResponseDTOS = groupMembers.stream()
                .map(m -> FindGroupMemberResponseDTO.builder()
                .id(m.getMember().getId()).build()).toList();
        return findGroupMemberResponseDTOS;
    }

    @Transactional
    public GroupMemberResponseDTO deleteGroupMember(final Long groupId, Long memberId){
        groupMemberRepository.deleteGroupMemberByGroup_GroupIdAndMember_Id(groupId, memberId);
        GroupMemberResponseDTO groupMemberResponseDTO = GroupMemberResponseDTO.builder()
                .groupId(groupId)
                .memberId(memberId)
                .build();
        return groupMemberResponseDTO;
    }

    public List<GroupMemberListResponseDTO> findGroupMembers(final Long memberId){
        final List<GroupMemberResponseDTO> groups = findGroupId(memberId);
        final List<GroupMemberListResponseDTO> dtoList = groups.stream().map(g -> {
            List<FindGroupMemberResponseDTO> friends = findGroupMemberId(g.getGroupId());
            return GroupMemberListResponseDTO.builder()
                .groupId(g.getGroupId())
                .groupName(g.getGroupName())
                .count(friends.size())
                .members(friends)
                .build();
        }).collect(Collectors.toList());

//        List<GroupMemberListResponseDTO> dtoList = new ArrayList<>();
//        for (GroupMemberResponseDTO g :groups){
//            List<FindGroupMemberResponseDTO> friends = findGroupMemberId(g.getGroupId());
//            GroupMemberListResponseDTO dto = GroupMemberListResponseDTO.builder().groupId(g.getGroupId())
//                    .groupName(groupService.findGroupName(g.getGroupId()))
//                    .count(friends.size())
//                    .members(friends);
//            dtoList.add(dto);
//        }
        return dtoList;
    }
}
