package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.group.dto.FindResponseGroupMemberDTO;
import kr.where.backend.group.dto.GroupCreateRequestDTO;
import kr.where.backend.group.dto.ResponseGroupMemberListDTO;
import kr.where.backend.group.dto.ResponseGroupMemberDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.Member;
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
    public ResponseGroupMemberDTO createGroupMember(final GroupCreateRequestDTO requestDTO, Long groupId, boolean isOwner){
        final Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));

        final GroupMember groupMember = new GroupMember(group, member, isOwner);
        groupMemberRepository.save(groupMember);

        final ResponseGroupMemberDTO createDTO = ResponseGroupMemberDTO.builder()
                .groupId(groupId)
                .groupName(requestDTO.getGroupName())
                .memberId(member.getId()).build();
        return createDTO;
    }


    public List<ResponseGroupMemberDTO> findGroupId(final Long memberId){
        final List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndIsOwner(memberId, true);

        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream().map(m ->
            ResponseGroupMemberDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getName())
                    .memberId(memberId).build()).toList();

//      List<GroupMemberResponseDTO> groupMemberResponseDTOS = groupMembers.stream().map(groupMember -> {
//            GroupMemberResponseDTO dto = new GroupMemberResponseDTO(groupMember.getGroup().getGroupId(), groupMember.getGroup().getName());
//            return dto;
//        }) .collect(Collectors.toList());
        return responseGroupMemberDTOS;
    }


    public List<FindResponseGroupMemberDTO> findGroupMemberId(final Long groupId){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(groupId);
        final List<FindResponseGroupMemberDTO> findResponseGroupMemberDTOS = groupMembers.stream()
                .map(m -> FindResponseGroupMemberDTO.builder()
                .id(m.getMember().getId()).build()).toList();
        return findResponseGroupMemberDTOS;
    }

    @Transactional
    public ResponseGroupMemberDTO deleteGroupMember(final Long groupId, Long memberId){
        groupMemberRepository.deleteGroupMemberByGroup_GroupIdAndMember_Id(groupId, memberId);
        ResponseGroupMemberDTO responseGroupMemberDTO = ResponseGroupMemberDTO.builder()
                .groupId(groupId)
                .memberId(memberId)
                .build();
        return responseGroupMemberDTO;
    }

    public List<ResponseGroupMemberListDTO> findGroupMembers(final Long memberId){
        final List<ResponseGroupMemberDTO> groups = findGroupId(memberId);
        final List<ResponseGroupMemberListDTO> dtoList = groups.stream().map(g -> {
            List<FindResponseGroupMemberDTO> friends = findGroupMemberId(g.getGroupId());
            return ResponseGroupMemberListDTO.builder()
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
