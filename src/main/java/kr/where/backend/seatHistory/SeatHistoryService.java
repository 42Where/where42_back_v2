package kr.where.backend.seatHistory;

import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
