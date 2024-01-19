package kr.where.backend.group;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
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
    public ResponseGroupMemberDTO createGroupMember(final CreateGroupMemberDTO requestDTO, final boolean isOwner, final AuthUserInfo authUser){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        //그룹의 주인이 만들어지는 경우는 멤버 컨트롤러에서만 호출되므로 api로 직접 그룹멤버를 만드는 false일때만 확인
        if(isOwner == false){
            isMyGroup(requestDTO.getGroupId(), authUser);
        }

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

    public void isMyGroup(Long groupId, AuthUserInfo authUser)
    {
        List<ResponseGroupMemberDTO> groups = findGroupsInfoByIntraId(authUser);
        if (groups.stream().map(ResponseGroupMemberDTO::getGroupId).noneMatch(g -> g.equals(groupId))) {
            throw new GroupException.CannotModifyGroupException();
        }
    }
    public List<ResponseGroupMemberDTO> findGroupsInfoByIntraId(final AuthUserInfo authUser){
        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = findGroupIdByIntraId(authUser.getIntraId());

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

    public List<ResponseOneGroupMemberDTO> findGroupMemberByGroupId(final Long groupId, final AuthUserInfo authUser){
        groupRepository.findById(groupId).orElseThrow(GroupException.NoGroupException::new);

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

    public List<ResponseGroupMemberListDTO> findMyAllGroupInformation(final AuthUserInfo authUser){
        final List<ResponseGroupMemberDTO> groups = findGroupIdByIntraId(authUser.getIntraId());

        // 더 나은 람다로 ㄱㄱ
        return groups.stream().map(g -> {
            List<ResponseOneGroupMemberDTO> friends = findGroupMemberByGroupId(g.getGroupId(), authUser);
            return ResponseGroupMemberListDTO.builder()
                    .groupId(g.getGroupId())
                    .groupName(g.getGroupName())
                    .count(friends.size())
                    .members(friends)
                    .build();
        }).toList();
    }

    @Transactional
    public void duplicateGroupMember(final Long groupId, final List<Member> members){
        final long count = groupMemberRepository.countByGroup_GroupIdAndMemberIn(groupId, members);
        if (count > 0)
            throw new GroupMemberException.DuplicatedGroupMemberException();
    }

    @Transactional
    public List<ResponseOneGroupMemberDTO> addFriendsList(final AddGroupMemberListDTO dto, final AuthUserInfo authUser){
        final Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        isMyGroup(dto.getGroupId(), authUser);
//        final List<Member> members = memberRepository.findByIntraNameIn(dto.getMembers())
//                .orElseThrow(MemberException.NoMemberException::new);
        final List<Member> members = memberRepository.findByIntraIdIn(dto.getMembers())
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
    public List<ResponseGroupMemberDTO> deleteFriendsList(final DeleteGroupMemberListDTO dto, final AuthUserInfo authUser){

        List<GroupMember> deleteGroupMember;

        groupRepository.findById(dto.getGroupId()).orElseThrow(GroupException.NoGroupException::new);
        isMyGroup(dto.getGroupId(), authUser);

        System.out.println(authUser.getDefaultGroupId() + " and " + dto.getGroupId());
        //지우려는 그룹이 기본 그룹이라면 해당 멤버의 모든 그룹에서 멤버 삭제
        if (authUser.getDefaultGroupId().equals(dto.getGroupId())){
            System.out.println("in?");
            final List<GroupMember> groupsOfOwner = groupMemberRepository
                    .findGroupMembersByMember_IntraIdAndIsOwner(authUser.getIntraId(), true);
            System.out.println("size : " + groupsOfOwner.size());
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

    public List<ResponseOneGroupMemberDTO> findMemberNotInGroup(final Long groupId, final AuthUserInfo authUser) {
        groupRepository.findById(authUser.getDefaultGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        final List<ResponseOneGroupMemberDTO> defaultMembers = findGroupMemberByGroupId(authUser.getDefaultGroupId(), authUser);
        final List<ResponseOneGroupMemberDTO> groupMembers = findGroupMemberByGroupId(groupId, authUser);

        final List<ResponseOneGroupMemberDTO> membersNotInGroup = defaultMembers.stream()
                .filter(defaultMember -> groupMembers.stream()
                        .noneMatch(groupMember -> defaultMember.getIntraId().equals(groupMember.getIntraId())))
                .toList();

        return membersNotInGroup;
    }
}
