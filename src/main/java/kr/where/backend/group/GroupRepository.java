package kr.where.backend.group;

import java.util.List;
import java.util.Optional;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findById(Long groupId);
}
