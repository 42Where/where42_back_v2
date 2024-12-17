package kr.where.backend.seatHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatHistoryRepository extends JpaRepository<SeatHistory, Long> {
    Optional<SeatHistory> findById(final Long id);
}
