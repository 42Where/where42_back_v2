package kr.where.backend.cluster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    Optional<Cluster> findByClusterAndRowIndexAndSeat(String c, Integer r, Integer s);

    @Modifying
    @Query("UPDATE Cluster c SET c.member = null")
    void updateAllMembersToNull();
}
