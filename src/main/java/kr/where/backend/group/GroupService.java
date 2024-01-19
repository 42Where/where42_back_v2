package kr.where.backend.group;

import java.util.List;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.group.dto.group.CreateGroupDTO;
import kr.where.backend.group.dto.groupmember.CreateGroupMemberDTO;
import kr.where.backend.group.dto.group.ResponseGroupDTO;
import kr.where.backend.group.dto.groupmember.ResponseGroupMemberDTO;
import kr.where.backend.group.dto.group.UpdateGroupDTO;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final MemberRepository memberRepository;

    /**
     * 그룹 생성
     * @param dto : CreateGroupDTO (groupName)
     * @param authUser : ContextHolder에 저장된 유저 정보
     * @return ResponeGroupDTO
     */
    @Transactional
    public ResponseGroupDTO createGroup(final CreateGroupDTO dto, final AuthUserInfo authUser){
        Group group = new Group(dto.getGroupName());
        groupRepository.save(group);

        CreateGroupMemberDTO createGroupMemberDTO = CreateGroupMemberDTO.builder()
                .intraId(authUser.getIntraId())
                .groupId(group.getGroupId()).build();
        groupMemberService.createGroupMember(createGroupMemberDTO, true, authUser);
        return ResponseGroupDTO.from(group);
    }

    /**
     * groupId로 group을 조회하는 메서드
     * 인자로 들어온 그룹이 존재하지 않을 경우 Exception
     * @param groupId
     * @return group
     */
    public Group findOneGroupById(final Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupException.NoGroupException::new);
        return group;
    }

    /**
     * 인자로 들어온 그룹의 이름을 반환
     * @param groupId
     * @return groupName
     */
    public final String findGroupNameById(final Long groupId){
        Group group = findOneGroupById(groupId);
        return group.getGroupName();
    }

    /**
     * 그룹 이름을 업데이트
     * @param dto : UpdateGroupDTO(groupId)
     * @param authUser
     * @return ResponseGroupDTO
     */
    @Transactional
    public ResponseGroupDTO updateGroup(final UpdateGroupDTO dto, final AuthUserInfo authUser) {
        updateGroupValidate(dto, authUser);
        Group group = findOneGroupById(dto.getGroupId());
        group.setGroupName(dto.getGroupName());
        return ResponseGroupDTO.from(group);
    }

    /**
     * 그룹 삭제
     * 삭제할 그룹이 본인의 그룹이라면 삭제 불가
     * @param groupId : 삭제할 그룹의 아이디
     * @param authUser
     * @return ResponseGroupDTO
     */
    @Transactional
    public ResponseGroupDTO deleteGroup(final Long groupId, final AuthUserInfo authUser) {
        isMyGroup(groupId, authUser);
        final Group group = findOneGroupById(groupId);
        //validate 추가
        groupRepository.delete(group);

        return ResponseGroupDTO.from(group);
    }

    /**
     * 인자로 들어온 그룹ID가 authUser가 가지고 있는 그룹인지 아닌지 확인
     * 아니라면 Exception
     * @param groupId
     * @param authUser
     */
    public final void isMyGroup(Long groupId, AuthUserInfo authUser) {
        List<ResponseGroupMemberDTO> groups = groupMemberService.findGroupsInfoByIntraId(authUser);
        if (groups.stream().map(ResponseGroupMemberDTO::getGroupId).noneMatch(g -> g.equals(groupId))) {
            throw new GroupException.CannotModifyGroupException();
        }
    }

    /**
     * group이름 변경 전 변경 할 권한이 있는지 확인,
     * 변경할 그룹이 기본그룹이라면 변경 불
     * @param dto
     * @param authUser
     */
    public final void updateGroupValidate(final UpdateGroupDTO dto, final AuthUserInfo authUser) {
        if (dto.getGroupId().equals(authUser.getDefaultGroupId()))
            throw new GroupException.CannotModifyGroupException();
        isMyGroup(dto.getGroupId(), authUser);
    }
}
