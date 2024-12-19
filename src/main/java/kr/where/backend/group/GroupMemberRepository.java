package kr.where.backend.group;

import java.util.List;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findGroupMembersByMemberAndIsOwner(Member member, Boolean isOwner);

    List<GroupMember> findGroupMemberByGroup_GroupIdAndIsOwnerIsFalse(Long groupId);

    List<GroupMember> findGroupMembersByGroup_GroupIdAndMember_IntraIdIn(Long groupId, List<Integer> MemberIntraId);

    boolean existsByGroupAndMember(Group group, Member member);

    long countByGroup_GroupIdAndMemberIn(Long groupId, List<Member> members);

    List<GroupMember> findGroupMembersByMember_IntraIdAndIsOwner(Integer intraId, boolean isOwner);

    List<GroupMember> findGroupMembersByGroup_GroupIdInAndMember_IntraIdIn(List<Long> groupIds, List<Integer> memberIds);

    List<GroupMember> findGroupMemberByGroup_GroupId(Long defaultGroupId);

    @Query("SELECT gm.member FROM GroupMember gm WHERE gm.group.groupId = :groupId")
    List<Member> findMembersByGroupId(@Param("groupId") final Long groupId);
}
