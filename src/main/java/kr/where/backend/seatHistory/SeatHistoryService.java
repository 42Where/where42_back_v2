package kr.where.backend.seatHistory;

import io.swagger.v3.oas.models.security.SecurityScheme;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
