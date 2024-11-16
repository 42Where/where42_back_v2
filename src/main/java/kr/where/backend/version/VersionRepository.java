package kr.where.backend.version;

import kr.where.backend.aspect.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    @QueryLog
    Optional<Version> findByOsType(String osType);
}
