package kr.where.backend.imacHistory;

import kr.where.backend.imacHistory.dto.GroupByImac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface ImacHistoryRepository extends JpaRepository<ImacHistory, Long> {
    Optional<ImacHistory> findById(final Long id);

    @Query(value = """
        SELECT intra_id, imac, COUNT(*), 
               CAST(SUM(EXTRACT(EPOCH FROM logout_at - login_at)) AS BIGINT)
        FROM imac_history
        WHERE intra_id =:intraId
        GROUP BY imac
        """,
        nativeQuery = true)
    List<Object []> findAllGroupByImac(@Param("intraId") final Integer intraId);

    List<ImacHistory> findAllByIntraId(final Integer intraId);
}
