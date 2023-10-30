package kr.where.backend.group;

import java.util.List;
import kr.where.backend.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByMemberIdAndIsOwner(Long memberId, boolean isOwner);

    List<GroupMember> findGroupMemberByGroup_GroupId(Long groupId);

    void deleteAllByGroup_GroupId(Long groupId);
}
