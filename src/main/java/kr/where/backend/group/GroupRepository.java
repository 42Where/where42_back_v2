package kr.where.backend.group;

import java.util.List;
import java.util.Optional;

import kr.where.backend.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findById(Long groupId);

    @Query("SELECT gm.group FROM GroupMember gm " +
            "WHERE gm.member.intraId = :intraId " +
            "AND gm.isOwner = true")
    List<Group> findAllGroupByMember(@Param("intraId") final Integer intraId);

}
