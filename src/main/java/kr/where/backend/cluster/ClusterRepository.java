package kr.where.backend.cluster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    Optional<Cluster> findByClusterAndRowIndexAndSeat(final String c, final Integer r, final Integer s);
}
