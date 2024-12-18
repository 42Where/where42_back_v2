package kr.where.backend.seatHistory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatHistoryRepository extends JpaRepository<SeatHistory, Long> {
    Optional<SeatHistory> findById(final Long id);

    @Query("SELECT sh FROM SeatHistory sh " +
            "WHERE sh.member.intraId = :intraId ORDER BY sh.count DESC")
    List<SeatHistory> findSeatHistoriesByMemberASCLimit(
            @Param("intraId") final Integer intraId,
            final Pageable pageable
    );

    @Query("SELECT SUM(sh.count) FROM SeatHistory sh WHERE sh.member.intraId = :intraId")
    int sumSeatHistoriesCountByMember(@Param("intraId") final Integer intraId);
}
