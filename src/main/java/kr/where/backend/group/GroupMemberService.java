package kr.where.backend.group;

import java.util.List;
import java.util.stream.Collectors;

import kr.where.backend.api.HaneApiService;
import kr.where.backend.auth.authUser.AuthUser;
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
    private final HaneApiService haneApiService;
    /**
     * 그룹에 그룹 멤버를 추가
     * 인자로 들어온 groupId가 존재하지 않다면 Exception
     * 인자로 들어온 memberId가 존재하지 않다면 Exception
     * isOnwer가  true인 경우는
     * member가 처음 생성될 때, 기본그룹을 만들 때 &&  그룹을 새로 생성 될때
     * 이므로 아직 그룹이 생성되지 않은 시기이므로 해당 그룹이 나의 그룹인지 검사X
     * 이미 인자로 들어온 group에 member가 포함되어 있다면 Exception
     * @param requestDTO : CreateGroupMemberDTO(그룹에 추가 할 intraId, 추가 할 groupId)
     * @param isOwner : isOwner로 자신이 그룹을 생성할 경우, 그룹에 포함될 경우를 구별
     * @param authUser
     * @return ResponseGroupMemberDTO
     */
    @Transactional
    public ResponseGroupMemberDTO createGroupMember(final CreateGroupMemberDTO requestDTO, final boolean isOwner, final AuthUser authUser){
        final Group group = groupRepository.findById(requestDTO.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        final Member member = memberRepository.findByIntraId(requestDTO.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);
        if (!isOwner) {
            isMyGroup(requestDTO.getGroupId(), authUser);
        }
        if (groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new GroupMemberException.DuplicatedGroupMemberException();
        }

        final GroupMember groupMember = new GroupMember(group, member, isOwner);
        groupMemberRepository.save(groupMember);

        return ResponseGroupMemberDTO.builder()
                .groupId(authUser.getDefaultGroupId())
                .groupName(group.getGroupName())
                .intraId(member.getIntraId())
                .memberIntraName(member.getIntraName())
                .location(member.getLocation().getLocation())
                .inCluster(member.isInCluster())
                .comment(member.getComment())
                .image(member.getImage()).build();
    }

    /**
     * 기본 그룹에 멤버를 추가하는 메서드
     * 일반그룹멤버 생성메서드와 기능 유사(기본 그룹멤버 api에 dto를 사용하지 않기 위해 사용)
     * @param intraId
     * @param isOwner
     * @param authUser
     * @return
     */
    @Transactional
    public ResponseGroupMemberDTO createDefaultGroupMember(final Integer intraId, final boolean isOwner, final AuthUser authUser){
        final Group group = groupRepository.findById(authUser.getDefaultGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        final Member member = memberRepository.findByIntraId(intraId)
                .orElseThrow(MemberException.NoMemberException::new);

        if (groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new GroupMemberException.DuplicatedGroupMemberException();
        }
        final GroupMember groupMember = new GroupMember(group, member, isOwner);
        groupMemberRepository.save(groupMember);

        return ResponseGroupMemberDTO.builder()
                .groupId(authUser.getDefaultGroupId())
                .groupName(group.getGroupName())
                .intraId(member.getIntraId())
                .memberIntraName(member.getIntraName())
                .location(member.getLocation().getLocation())
                .inCluster(member.isInCluster())
                .comment(member.getComment())
                .image(member.getImage()).build();
    }

    /**
     * 해당 그룹이 authUser가 가지고 있는 그룹인지 확인
     * @param groupId
     * @param authUser
     */
    public void isMyGroup(final Long groupId, final AuthUser authUser) {
        final List<ResponseGroupMemberDTO> groups = findGroupsInfoByIntraId(authUser);
        if (groups.stream().map(ResponseGroupMemberDTO::getGroupId).noneMatch(g -> g.equals(groupId))) {
            throw new GroupException.CannotModifyGroupException();
        }
    }

    /**
     * 멤버가 가진 그룹의 정보를 반환
     * @param authUser
     * @return List<ResponseGroupMemberDTO>
     */
    public List<ResponseGroupMemberDTO> findGroupsInfoByIntraId(final AuthUser authUser) {

        return findGroupIdByIntraId(authUser.getIntraId());
    }

    /**
     * intraId로 해당 멤버의 그룹 조회
     * intraId가 존재하지 않는다면 Exception
     * 그룹멤버 중에서 IsOwner가 true이고,
     * member가 매치가 되는 그룹멤버 리스트를 조회 후 필요한 정보 파싱
     * @param intraId
     * @return List<ResponseGroupMemberDTO>
     */
    public List<ResponseGroupMemberDTO> findGroupIdByIntraId(final Integer intraId) {
        final Member owner = memberRepository.findByIntraId(intraId)
                                .orElseThrow(MemberException.NoMemberException::new);
        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMembersByMemberAndIsOwner(owner, true);

        final List<ResponseGroupMemberDTO> responseGroupMemberDTOS = groupMembers.stream().map(m ->
            ResponseGroupMemberDTO.builder()
                    .groupId(m.getGroup().getGroupId())
                    .groupName(m.getGroup().getGroupName())
                    .intraId(intraId).build()).toList();
        return responseGroupMemberDTOS;
    }

    /**
     * groupId를 받아 해당 그룹의 모든 그룹멤버 리스트 반환
     * 해당 그룹이 존재 하지 않는다면 Exception
     * @param groupId
     * @return List<ResponseOneGroupMemberDTO>
     */
    public List<ResponseOneGroupMemberDTO> findGroupMemberByGroupId(final Long groupId) {
        groupRepository.findById(groupId).orElseThrow(GroupException.NoGroupException::new);

        final List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByGroup_GroupIdAndIsOwnerIsFalse(groupId);

        // 하네 업데이트

        return groupMembers.stream()
            .peek(m -> haneApiService.updateInClusterOne(m.getMember()))
            .map(m -> ResponseOneGroupMemberDTO.builder()
                .intraId(m.getMember().getIntraId())
                .image(m.getMember().getImage())
                .comment(m.getMember().getComment())
                .intraName(m.getMember().getIntraName())
                .inCluster(m.getMember().isInCluster())
                .location(m.getMember().getLocation().getLocation())
                .build()
            ).toList();
    }

    /**
     * 메인 페이지에 표시해줄 모든 그룹, 그룹멤버에 대한 정보 반환
     * @param authUser
     * @return List<ResponseGroupMemberListDTO>
     */
    @Transactional
    public List<ResponseGroupMemberListDTO> findMyAllGroupInformation(final AuthUser authUser){
        final List<ResponseGroupMemberDTO> groups = findGroupIdByIntraId(authUser.getIntraId());

        return groups.stream().map(g -> {
            List<ResponseOneGroupMemberDTO> friends = findGroupMemberByGroupId(g.getGroupId());
            return ResponseGroupMemberListDTO.builder()
                    .groupId(g.getGroupId())
                    .groupName(g.getGroupName())
                    .count(friends.size())
                    .members(friends)
                    .build();
        }).toList();
    }

    /**
     * 인자로 들어온 member리스트가 중 이미 그룹에 저장되어 있는 멤버라면 Exception
     * @param groupId
     * @param members
     */
    @Transactional
    public void duplicateGroupMember(final Long groupId, final List<Member> members){
        final long count = groupMemberRepository.countByGroup_GroupIdAndMemberIn(groupId, members);
        if (count > 0)
            throw new GroupMemberException.DuplicatedGroupMemberException();
    }

    /**
     * groupId에 해당하는 그룹에 멤버 리스트를 추가
     * groupId에 해당하는 그룹이 없다면 Exception
     * groupId가 authUser가 소유햔 그룹에 포함되는지 확인
     * 이미 저장되어있는 멤버인지 중복확인
     * @param dto : AddGroupMemberListDTO (groupId, List<Member>)
     * @param authUser
     * @return List<ResponseOneGroupMemberDTO>
     */
    @Transactional
    public List<ResponseOneGroupMemberDTO> addFriendsList(final AddGroupMemberListDTO dto, final AuthUser authUser){
        final Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        isMyGroup(dto.getGroupId(), authUser);
        final List<Member> members = memberRepository.findByIntraIdIn(dto.getMembers())
                .orElseThrow(MemberException.NoMemberException::new);
        duplicateGroupMember(dto.getGroupId(), members);

        final List<GroupMember> groupMembers = members.stream()
                .map(member -> new GroupMember(group, member, false))
                .collect(Collectors.toList());

        groupMemberRepository.saveAll(groupMembers);

        return groupMembers.stream()
                .map(m -> ResponseOneGroupMemberDTO.builder()
                        .intraId(m.getMember().getIntraId())
                        .intraName(m.getMember().getIntraName())
                        .location(m.getMember().getLocation().getLocation())
                        .inCluster(m.getMember().isInCluster())
                        .comment(m.getMember().getComment())
                        .image(m.getMember().getImage())
                        .build()).toList();
    }

    /**
     * 그룹에 포함된 그룹멤버 삭제
     * 만약 지울 그룹이 멤버의 기본그룹이라면 멤버가 소유한 그룹에 모든 멤버 삭제
     * @param dto : DeleteGroupMemberListDTO (groupId, List<member>)
     * @param authUser
     * @return List<ResponseGroupMemberDTO>
     */
    @Transactional
    public List<ResponseGroupMemberDTO> deleteFriendsList(final DeleteGroupMemberListDTO dto, final AuthUser authUser){

        final List<GroupMember> deleteGroupMember;

        groupRepository.findById(dto.getGroupId()).orElseThrow(GroupException.NoGroupException::new);
        isMyGroup(dto.getGroupId(), authUser);

        if (authUser.getDefaultGroupId().equals(dto.getGroupId())) {
            final List<GroupMember> groupsOfOwner = groupMemberRepository
                    .findGroupMembersByMember_IntraIdAndIsOwner(authUser.getIntraId(), true);
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
                        .memberIntraName(m.getMember().getIntraName())
                        .location(m.getMember().getLocation().getLocation())
                        .inCluster(m.getMember().isInCluster())
                        .comment(m.getMember().getComment())
                        .image(m.getMember().getImage())
                        .build()).toList();
    }

    /**
     * 기본그룹에 포함 되지 않은 멤버 리스트 반환
     * (다른 일반 그룹에 멤버 추가 시, 해당 그룹에 포함되지 않은 친구 리스트를 보여줘야 하기때문에)
     * @param groupId
     * @param authUser
     * @return List<ResponseOneGroupMemberDTO>
     */
    public List<ResponseOneGroupMemberDTO> findMemberNotInGroup(final Long groupId, final AuthUser authUser) {
        groupRepository.findById(authUser.getDefaultGroupId())
                .orElseThrow(GroupException.NoGroupException::new);
        groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        final List<ResponseOneGroupMemberDTO> defaultMembers = findGroupMemberByGroupId(authUser.getDefaultGroupId());
        final List<ResponseOneGroupMemberDTO> groupMembers = findGroupMemberByGroupId(groupId);

        return defaultMembers.stream()
                .filter(defaultMember -> groupMembers.stream()
                        .noneMatch(groupMember -> defaultMember.getIntraId().equals(groupMember.getIntraId())))
                .toList();
    }
}
