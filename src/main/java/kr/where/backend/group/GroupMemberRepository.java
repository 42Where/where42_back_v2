package kr.where.backend.group;

import java.util.List;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import kr.where.backend.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
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

//    @Query("SELECT new kr.where.backend.group.dto.groupmember.ResponseGroupMemberListDTO(" +
//            "g.groupId, g.groupName, COUNT(gm), " +
//            "new kr.where.backend.group.dto.groupmember.ResponseOneGroupMemberDTO(m.intraId, m.intraName, m.grade, m.image, m.comment, m.inCluster, m.agree, m.defaultGroupId, l.location)) " +
//            "FROM Group g " +
//            "JOIN g.groupMembers gm " +
//            "JOIN gm.member m " +
//            "LEFT JOIN m.location l " +
//            "WHERE g.groupId IN (" +
//            "    SELECT gmInner.group.groupId FROM GroupMember gmInner " +
//            "    WHERE gmInner.member.intraId = :intraId AND gmInner.isOwner = true" +
//            ") AND gm.isOwner = false " +
//            "GROUP BY g.groupId, g.groupName, m.intraId, m.intraName, m.grade, m.image, m.comment, m.inCluster, m.agree, m.defaultGroupId, l.location")
//    List<ResponseGroupMemberListDTO> findAllByOwnerIntraId(@Param("intraId")final Integer intraId);
}
