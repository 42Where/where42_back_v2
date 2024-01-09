package kr.where.backend.group;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.group.dto.groupmember.*;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.group.exception.GroupMemberException;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.Member;
import kr.where.backend.member.exception.MemberException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public ResponseGroupMemberDTO createGroupMember(final CreateGroupMemberDTO requestDTO, final boolean isOwner){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);

        boolean isGroupMemberExists = groupMemberRepository.existsByGroupAndMember(group, member);
        if (isGroupMemberExists) {
            throw new GroupMemberException.DuplicatedGroupMemberException();
        }
        final GroupMember groupMember = new GroupMember(group, member, isOwner);
        groupMemberRepository.save(groupMember);

        final ResponseGroupMemberDTO responseGroupMemberDTO = ResponseGroupMemberDTO.builder()
                .groupId(requestDTO.getGroupId())
                .groupName(group.getGroupName())
                .intraId(member.getIntraId()).build();
        return responseGroupMemberDTO;
    }

    public List<ResponseGroupMemberDTO> findGroupsInfoByIntraId(final Integer intraId){
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = findGroupIdByIntraId(intraId);

        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberDTO> findGroupIdByIntraId(final Integer intraId){
        final Member owner = memberRepository.findByIntraId(intraId)
                                .orElseThrow(MemberException.NoMemberException::new);
        final List<GroupMember> groupMembers = groupMemberRepository
                .findGroupMembersByMemberAndIsOwner(owner, true);
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream().map(m ->
            ResponseGroupMemberDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getGroupName())
                    .intraId(intraId).build()).toList();

        return responseGroupMemberDTOS;
    }

    public List<ResponseOneGroupMemberDTO> findGroupMemberbyGroupId(final Long groupId){
        final Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupIdAndIsOwnerIsFalse(groupId);
        final List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseOneGroupMemberDTO.builder()
                .intraId(m.getMember().getIntraId())
                .image(m.getMember().getImage())
                .comment(m.getMember().getComment())
                .intraName(m.getMember().getIntraName())
                .inCluster(m.getMember().isInCluster())
                .location(m.getMember().getLocation().getLocation())
                .build()).toList();

        return responseGroupMemberDTOS;
    }

    public List<ResponseGroupMemberListDTO> findMyAllGroupInformation(final Integer intraId){
        final List<ResponseGroupMemberDTO> groups = findGroupIdByIntraId(intraId);
        final List<ResponseGroupMemberListDTO> responseGroupMemberListDTOS = groups.stream().map(g -> {
            List<ResponseOneGroupMemberDTO> friends = findGroupMemberbyGroupId(g.getGroupId());
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
    public void duplicateGroupMember(final Long groupId, final List<Member> members){
        final long count = groupMemberRepository.countByGroup_GroupIdAndMemberIn(groupId, members);
        if (count > 0)
            throw new GroupMemberException.DuplicatedGroupMemberException();
    }

    @Transactional
    public List<ResponseOneGroupMemberDTO> addFriendsList(final AddGroupMemberListDTO dto){
        final Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        final List<Member> members = memberRepository.findByIntraNameIn(dto.getMembers())
                .orElseThrow(MemberException.NoMemberException::new);
        duplicateGroupMember(dto.getGroupId(), members);

        final List<GroupMember> groupMembers = members.stream()
                .map(member -> new GroupMember(group, member, false))
                .collect(Collectors.toList());

        groupMemberRepository.saveAll(groupMembers);

        final List<ResponseOneGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream()
                .map(m -> ResponseOneGroupMemberDTO.builder()
                        .intraId(m.getMember().getIntraId())
                        .intraName(m.getMember().getIntraName())
                        .build()).toList();
        return responseGroupMemberDTOS;
    }

    @Transactional
    public List<ResponseGroupMemberDTO> deleteFriendsList(final DeleteGroupMemberListDTO dto){

        // 그룹 아이디를 받으니까 그 아이디로 그룹의 주인을 찾고//
        // 그 주인의 기본그룹 아이디와 받은 그룹id가 같다면
        // 그 주인의 모든 그룹에서 해당 멤버를 찾아서 삭제
        List<GroupMember> deleteGroupMember;

        groupRepository.findById(dto.getGroupId()).orElseThrow(GroupException.NoGroupException::new);
        final GroupMember owner = groupMemberRepository
                .findGroupMemberByGroup_GroupIdAndIsOwner(dto.getGroupId(), true);

        if (owner.getMember().getDefaultGroupId() == dto.getGroupId()){
            final List<GroupMember> groupsOfOwner = groupMemberRepository
                    .findGroupMembersByMember_IntraIdAndIsOwner(owner.getMember().getIntraId(), true);

            final List<Long> groups = groupsOfOwner.stream()
                    .map(g -> g.getGroup().getGroupId())
                    .toList();

            deleteGroupMember = groupMemberRepository
                    .findGroupMembersByGroup_GroupIdInAndMember_IntraIdIn(groups,dto.getMembers());
            groupMemberRepository.deleteAll(deleteGroupMember);
        }
        else {
            deleteGroupMember = groupMemberRepository
                    .findGroupMembersByGroup_GroupIdAndMember_IntraIdIn(dto.getGroupId(), dto.getMembers());
            groupMemberRepository.deleteAll(deleteGroupMember);
        }

        return deleteGroupMember.stream()
                .map(m -> ResponseGroupMemberDTO.builder()
                        .groupId(dto.getGroupId())
                        .intraId(m.getMember().getIntraId())
                        .build()).toList();
    }

    public List<ResponseOneGroupMemberDTO> findMemberNotInGroup(final Long default_groupId, final Long groupId) {
        final Group default_group = groupRepository.findById(default_groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        final Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        final List<ResponseOneGroupMemberDTO> defaultMembers = findGroupMemberbyGroupId(default_groupId);
        final List<ResponseOneGroupMemberDTO> groupMembers = findGroupMemberbyGroupId(groupId);

        final List<ResponseOneGroupMemberDTO> membersNotInGroup = defaultMembers.stream()
                .filter(defaultMember -> groupMembers.stream()
                        .noneMatch(groupMember -> defaultMember.getIntraId().equals(groupMember.getIntraId())))
                .toList();

        return membersNotInGroup;
    }
}
