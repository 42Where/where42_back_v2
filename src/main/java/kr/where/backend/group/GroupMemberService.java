package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.group.dto.group.FindGroupDto;
import kr.where.backend.group.dto.groupmember.*;
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
    private final GroupRepository groupRepository;

    @Transactional
    public ResponseGroupMemberDTO createGroupMember(final CreateGroupMemberDTO requestDTO){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));

        final GroupMember groupMember = new GroupMember(group, member, requestDTO.isOwner());
        groupMemberRepository.save(groupMember);

        final ResponseGroupMemberDTO createDTO = ResponseGroupMemberDTO.builder()
                .groupId(requestDTO.getGroupId())
                .groupName(requestDTO.getGroupName())
                .memberId(member.getId()).build();
        return createDTO;
    }

    public ResponseGroupMemberListDTO addGroupMember(final AddGroupMemberListDTO requestDTO){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        ResponseGroupMemberListDTO responseGroupMemberListDTO;
        for (String m : requestDTO.getMembers()){
            final Member member = memberRepository.findByIntraName(m).orElseThrow();
            final GroupMember groupMember = new GroupMember(group, member, false);
            groupMemberRepository.save(groupMember);
            ResponseGroupMemberDTO memberDTO = ResponseGroupMemberDTO.builder().memberId(member.getId()).build();
            responseGroupMemberListDTO = ResponseGroupMemberListDTO.builder().groupId(requestDTO.getGroupId()).build();
        }
        return responseGroupMemberListDTO;
        //이거 매우 잘못됨 수정해야함. 특정 그룹에 그룹멤버여러명 추가 하는 기능
    }


    public List<ResponseGroupMemberDTO> findGroupId(final FindGroupDto requsetDTO){
        final List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndIsOwner(requsetDTO.getMemberId(), true);

        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream().map(m ->
            ResponseGroupMemberDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getGroupName())
                    .memberId(requsetDTO.getMemberId()).build()).toList();

//      List<GroupMemberResponseDTO> groupMemberResponseDTOS = groupMembers.stream().map(groupMember -> {
//            GroupMemberResponseDTO dto = new GroupMemberResponseDTO(groupMember.getGroup().getGroupId(), groupMember.getGroup().getName());
//            return dto;
//        }) .collect(Collectors.toList());
        return responseGroupMemberDTOS;
    }


    public List<ResponseGroupMemberDTO> findGroupMemberId(final FindGroupMemberDto requsetDTO){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(requsetDTO.getGroupId());
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseGroupMemberDTO.builder()
                .memberId(m.getMember().getId()).build()).toList();
        return responseGroupMemberDTOS;
    }

    @Transactional
    public ResponseGroupMemberDTO deleteGroupMember(final RequestGroupMemberDTO requestDto){
        groupMemberRepository.deleteGroupMemberByGroup_GroupIdAndMember_Id(requestDto.getGroupId(), requestDto.getMemberId());
        ResponseGroupMemberDTO responseGroupMemberDTO = ResponseGroupMemberDTO.builder()
                .groupId(requestDto.getGroupId())
                .memberId(requestDto.getMemberId())
                .build();
        return responseGroupMemberDTO;
    }

    public List<ResponseGroupMemberListDTO> findGroupMembers(final FindGroupMemberDto requestDto){
//        RequestGroupMemberDTO searchMemberDto = RequestGroupMemberDTO.builder().memberId(requestDto.getMemberId()).build();
        FindGroupDto groupDto = FindGroupDto.builder().memberId(requestDto.getMemberId()).build();
        final List<ResponseGroupMemberDTO> groups = findGroupId(groupDto);
        final List<ResponseGroupMemberListDTO> dtoList = groups.stream().map(g -> {
            RequestGroupMemberDTO searchGroupDto = RequestGroupMemberDTO.builder().groupId(g.getGroupId()).build();
            List<ResponseGroupMemberDTO> friends = findGroupMemberId(requestDto);
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
