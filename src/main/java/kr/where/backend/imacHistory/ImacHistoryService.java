package kr.where.backend.imacHistory;

import kr.where.backend.imacHistory.dto.GroupByImac;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImacHistoryService {
    private final ImacHistoryRepository imacHistoryRepository;

    @Transactional
    public Long create(final Integer intraId, final String imac, final String login, final String logout) {
        final ImacHistory imacHistory = new ImacHistory(intraId, imac, login, logout);
        imacHistoryRepository.save(imacHistory);

        return imacHistory.getId();
    }

    public List<GroupByImac> getTotalUsageTimeByImac(final Integer intraId) {
        final List<Object []> imacList = imacHistoryRepository.findAllGroupByImac(intraId);
        return imacList.stream()
                .map(row -> new GroupByImac(
                        (Integer) row[0],
                        (String) row[1],
                        (Long) row[2],
                        (Long) row[3])
                )
                .collect(Collectors.toList());
    }

    public List<GroupByImac> getTotalUsageTimeByJpa(final Integer intraId) {
        final List<ImacHistory> imacHistories = imacHistoryRepository.findAllByIntraId(intraId);

        return imacHistories.stream().collect(Collectors.groupingBy(ImacHistory::getImac, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> {
                    String imac = entry.getKey();
                    List<ImacHistory> histories = entry.getValue();

                    long count = histories.size();

                    long usageTime = histories.stream().mapToLong(ImacHistory::usedImacTime).sum();

                    return new GroupByImac(intraId, imac, count, usageTime);
                })
                .collect(Collectors.toList());
    }
}
