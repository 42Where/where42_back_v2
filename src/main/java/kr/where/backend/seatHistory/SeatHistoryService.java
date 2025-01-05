package kr.where.backend.seatHistory;

import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.seatHistory.dto.ResponseHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatHistoryService {
    private final SeatHistoryRepository seatHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(final String seat, final Integer intraId) {
        final Member member = memberRepository.findByIntraId(intraId)
                .orElseThrow(MemberException.NoMemberException::new);

        final SeatHistory seatHistory = new SeatHistory(seat, member);
        seatHistoryRepository.save(seatHistory);

        member.getSeatHistories().add(seatHistory);
        return seatHistory.getId();
    }

    @Transactional
    public void report(final String seat, final Integer intraId) {
        final Member member = memberRepository.findByIntraId(intraId)
                .orElseThrow(MemberException.NoMemberException::new);

        member.getSeatHistories()
                .stream()
                .filter(s -> Objects.equals(s.getImac(), seat))
                .findFirst()
                .ifPresentOrElse(SeatHistory::increaseCount, () -> create(seat, intraId));
    }

    public List<ResponseHistoryDTO> getPopularSeatHistory(final Integer intraId) {
        final List<SeatHistory> seatHistories = seatHistoryRepository
                .findSeatHistoriesByMemberASCLimit(intraId, PageRequest.of(0, 3));
        final int size = seatHistoryRepository.sumSeatHistoriesCountByMember(intraId);

        return seatHistories.stream()
                .map(s -> new ResponseHistoryDTO(
                        s.getImac(),
                        Math.round(((double) s.getCount() / size) * 100 * 10.0) / 10.0)
                )
                .toList();
    }
}
