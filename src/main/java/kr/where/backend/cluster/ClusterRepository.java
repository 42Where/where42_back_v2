package kr.where.backend.cluster;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    Optional<Cluster> findByClusterAndRowIndexAndSeat(final String c, final Integer r, final Integer s);

    @Query("SELECT c FROM Cluster c ORDER BY c.usedCount DESC")
    List<Cluster> findTopNByOrderByUsedCountDesc(Pageable pageable);

    List<Cluster> findTop3ByOrderByUsedCountDesc();

}
