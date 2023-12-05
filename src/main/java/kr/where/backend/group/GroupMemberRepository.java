package kr.where.backend.group;

import java.util.List;
import java.util.Optional;

import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByMember_IdAndIsOwner(Long memberId, Boolean isOwner);

    List<GroupMember> findGroupMembersByMemberAndIsOwner(Member member, Boolean isOwner);

    List<GroupMember> findGroupMemberByGroup_GroupId(Long groupId);

    List<GroupMember> findGroupMembersByGroup_GroupIdAndMember_IntraIdIn(Long groupId, List<Long> MemberIntraId);

    List<GroupMember> deleteGroupMemberByGroup_GroupIdAndMember_Id(Long groupId, Long memberId);

    boolean existsByGroupAndMember(Group group, Member member);

    void deleteAllByGroup_GroupId(Long groupId);

    long countByGroup_GroupIdAndMemberIn(Long groupId, List<Member> members);
}
