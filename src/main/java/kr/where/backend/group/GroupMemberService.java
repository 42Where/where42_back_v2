package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
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

        boolean isGroupMemberExists = groupMemberRepository.existsByGroupAndMember(group, member);
        if (isGroupMemberExists) {
            throw new EntityNotFoundException("이미 그룹 멤버로 등록된 사용자입니다.");
        }
        System.out.println(requestDTO.isOwner());
        final GroupMember groupMember = new GroupMember(group, member, requestDTO.isOwner());
        groupMemberRepository.save(groupMember);

        final ResponseGroupMemberDTO createDTO = ResponseGroupMemberDTO.builder()
                .groupId(requestDTO.getGroupId())
                .groupName(requestDTO.getGroupName())
                .memberId(member.getIntraId()).build();
        return createDTO;
    }

//    @Transactional
//    public ResponseGroupMemberDTO createDefaultGroupMember(final CreateGroupMemberDTO requestDTO){
//        List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndIsOwner(requestDTO.getIntraId(), true);
//        Optional<Group> group = groupRepository.findByGroupMembersAndDefault(groupMembers, true);
//        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
//                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));
//
//        final GroupMember groupMember = new GroupMember(group.orElseThrow(), member, requestDTO.isOwner());
//        groupMemberRepository.save(groupMember);
//        final ResponseGroupMemberDTO createDTO = ResponseGroupMemberDTO.builder()
//                .groupId(requestDTO.getGroupId())
//                .groupName(requestDTO.getGroupName())
//                .memberId(member.getId()).build();
//
//        return createDTO;
//    }
//    기본그룹에 친구를 추가...하는건데.. 제대로 돌아가는가..?

    @Transactional
    public ResponseGroupMemberListDTO addGroupMember(final AddGroupMemberListDTO requestDTO){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        ResponseGroupMemberListDTO responseGroupMemberListDTO = ResponseGroupMemberListDTO.builder().build();
//        for (String m : requestDTO.getMembers()){
//            final Member member = memberRepository.findByIntraName(m).orElseThrow();
//            final GroupMember groupMember = new GroupMember(group, member, false);
//            groupMemberRepository.save(groupMember);
//            ResponseGroupMemberDTO memberDTO = ResponseGroupMemberDTO.builder().memberId(member.getId()).build();
//            responseGroupMemberListDTO = ResponseGroupMemberListDTO.builder().groupId(requestDTO.getGroupId()).build();
//        }

        // 수환 로직이에요 한번 보고 괜찮으면 채택 ㄱ
        final List<Member> members = memberRepository.findByIntraNameIn(requestDTO.getMembers());

        members.forEach(member -> {
            GroupMember groupMember = new GroupMember(group, member, false);

            groupMemberRepository.save(groupMember);
            responseGroupMemberListDTO.getMembers()
                    .add(ResponseGroupMemberDTO.builder().groupId(group.getGroupId()).memberId(member.getIntraId()).build());
        });

        return responseGroupMemberListDTO;
        // 특정 그룹에 그룹멤버여러명 추가 하는 기능 , 기본그룹에 있는지 확인해야함.
    }

//    public List<ResponseGroupMemberDTO> findGroupsInfo(final FindGroupDto request){
//        List<ResponseGroupMemberDTO> dto = findGroupId(request.getMemberId());
//
//        return dto;
//    }
    public List<ResponseGroupMemberDTO> findGroupsInfo(final Long memberId){
//        List<ResponseGroupMemberDTO> dto = findGroupId(request.getMemberId());
        List<ResponseGroupMemberDTO> dto = findGroupId(memberId);

        return dto;
    }

    public List<ResponseGroupMemberDTO> findGroupId(final Long memberId){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByMember_IntraIdAndIsOwner(memberId, true);
        // System.out.println(groupMembers.get(0).toString());
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream().map(m ->
            ResponseGroupMemberDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getGroupName())
                    .memberId(memberId).build()).toList();

        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberDTO> findGroupMemberbyGroupId(final Long groupId){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupId(groupId);
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseGroupMemberDTO.builder()
                .memberId(m.getMember().getIntraId())
                .image(m.getMember().getImage())
                .comment(m.getMember().getComment())
                .memberIntraName(m.getMember().getIntraName())
                .clusterLocation(m.getMember().getClusterLocation())
                .inCluster(m.getMember().isInCluster())
                .imacLocation(m.getMember().getImacLocation())
                        .build()).toList();

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

    public List<ResponseGroupMemberListDTO> findAllGroupInformation(final Long memberId){
        final List<ResponseGroupMemberDTO> groups = findGroupId(memberId);
        final List<ResponseGroupMemberListDTO> dtoList = groups.stream().map(g -> {
            List<ResponseGroupMemberDTO> friends = findGroupMemberbyGroupId(g.getGroupId());
            System.out.println("groupId : " + g.getGroupId());
            return ResponseGroupMemberListDTO.builder()
                    .groupId(g.getGroupId())
                    .groupName(g.getGroupName())
                    .count(friends.size())
                    .members(friends)
                    .build();
        }).collect(Collectors.toList());

        return dtoList;
    }

    public List<ResponseGroupMemberListDTO> findGroupMembersbyMemberId(final Long memberId){
//        RequestGroupMemberDTO searchMemberDto = RequestGroupMemberDTO.builder().memberId(requestDto.getMemberId()).build();
        FindGroupDto groupDto = FindGroupDto.builder().memberId(memberId).build();
        final List<ResponseGroupMemberDTO> groups = findGroupId(groupDto.getMemberId());
        final List<ResponseGroupMemberListDTO> dtoList = groups.stream().map(g -> {
            RequestGroupMemberDTO searchGroupDto = RequestGroupMemberDTO.builder().groupId(g.getGroupId()).build();
            List<ResponseGroupMemberDTO> friends = findGroupMemberbyGroupId(g.getGroupId());
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
        //수정해야함
    }

    @Transactional
    public List<ResponseGroupMemberDTO> addFriendsList(AddGroupMemberListDTO dto){
        List<String> memberId = dto.getMembers();
        Long groupId = dto.getGroupId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));

        List<Member> members = memberRepository.findByIntraNameIn(memberId);

        List<GroupMember> groupMembers = members.stream()
                .map(member -> new GroupMember(group, member, false))
                .collect(Collectors.toList());

        groupMemberRepository.saveAll(groupMembers);

        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseGroupMemberDTO.builder()
                        .memberId(m.getMember().getIntraId())
                        .memberIntraName(m.getMember().getIntraName())
                        .build()).toList();
        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberDTO> findMemberNotInGroup(FindGroupMemberDto dto)
    {
        List<ResponseGroupMemberDTO> defaultMembers = findGroupMemberbyGroupId(dto.getDefaultGroupId());
        List<ResponseGroupMemberDTO> groupMembers = findGroupMemberbyGroupId(dto.getGroupId());

        List<ResponseGroupMemberDTO> membersNotInGroup = defaultMembers.stream()
                .filter(defaultMember -> groupMembers.stream()
                        .noneMatch(groupMember -> defaultMember.getMemberId().equals(groupMember.getMemberId())))
                .collect(Collectors.toList());

        return membersNotInGroup;
    }
}
