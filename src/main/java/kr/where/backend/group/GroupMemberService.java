package kr.where.backend.group;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public ResponseGroupMemberDTO createGroupMember(final CreateGroupMemberDTO requestDTO){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
//        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
//                .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId()).orElseThrow();

        boolean isGroupMemberExists = groupMemberRepository.existsByGroupAndMember(group, member);
        if (isGroupMemberExists) {
            throw new EntityNotFoundException("이미 그룹 멤버로 등록된 사용자입니다.");
        }
        final GroupMember groupMember = new GroupMember(group, member, requestDTO.isOwner());
        groupMemberRepository.save(groupMember);

        final ResponseGroupMemberDTO responseGroupMemberDTO = ResponseGroupMemberDTO.builder()
                .groupId(requestDTO.getGroupId())
                .groupName(requestDTO.getGroupName())
                .memberId(member.getIntraId()).build();
        return responseGroupMemberDTO;
    }

    public List<ResponseGroupMemberDTO> findGroupsInfoByMemberId(final Long memberId){
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = findGroupIdByMemberId(memberId);

        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberDTO> findGroupIdByMemberId(final Long memberId){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByMember_IntraIdAndIsOwner(memberId, true);
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
                .inCluster(m.getMember().isInCluster())
                        .build()).toList();
//                .clusterLocation(m.getMember().getClusterLocation())
//                .imacLocation(m.getMember().getImacLocation())

        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberListDTO> findMyAllGroupInformation(final Long memberId){
        final List<ResponseGroupMemberDTO> groups = findGroupIdByMemberId(memberId);
        final List<ResponseGroupMemberListDTO> responseGroupMemberListDTOS = groups.stream().map(g -> {
            List<ResponseGroupMemberDTO> friends = findGroupMemberbyGroupId(g.getGroupId());
            return ResponseGroupMemberListDTO.builder()
                    .groupId(g.getGroupId())
                    .groupName(g.getGroupName())
                    .count(friends.size())
                    .members(friends)
                    .build();
        }).toList();

        return responseGroupMemberListDTOS;
    }

    @Transactional
    public Boolean duplicateGroupMember(final Long groupId, final List<Member> members){
        final long count = groupMemberRepository.countByGroup_GroupIdAndMemberIn(groupId, members);
        return count > 0;
    }

    @Transactional
    public List<ResponseGroupMemberDTO> addFriendsList(final AddGroupMemberListDTO dto){
        final Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("해당 그룹이 존재하지 않습니다."));
        final List<Member> members = memberRepository.findByIntraNameIn(dto.getMembers())
                .orElseThrow(()-> new EntityNotFoundException("해당 멤버가 존재하지 않습니다."));
        if (duplicateGroupMember(dto.getGroupId(), members))
            throw new IllegalArgumentException("이미 추가되어있는 멤버가 존재합니다.");

        final List<GroupMember> groupMembers = members.stream()
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

    @Transactional
    public List<ResponseGroupMemberDTO> deleteFriendsList(final DeleteGroupMemberListDto dto){
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByGroup_GroupIdAndMember_IntraIdIn(dto.getGroupId(), dto.getMembers());
        groupMemberRepository.deleteAll(groupMembers);

        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseGroupMemberDTO.builder()
                        .groupId(dto.getGroupId())
                        .memberId(m.getMember().getIntraId())
                        .build()).toList();
        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberDTO> findMemberNotInGroup(final FindGroupMemberDto dto) {
        final List<ResponseGroupMemberDTO> defaultMembers = findGroupMemberbyGroupId(dto.getDefaultGroupId());
        final List<ResponseGroupMemberDTO> groupMembers = findGroupMemberbyGroupId(dto.getGroupId());

        final List<ResponseGroupMemberDTO> membersNotInGroup = defaultMembers.stream()
                .filter(defaultMember -> groupMembers.stream()
                        .noneMatch(groupMember -> defaultMember.getMemberId().equals(groupMember.getMemberId())))
                .collect(Collectors.toList());

        return membersNotInGroup;
    }
}
