package kr.where.backend.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.where.backend.api.json.hane.HaneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void updateMemberInOrOutStatus(List<HaneResponseDto> haneResponseDtos) {
        if (haneResponseDtos == null || haneResponseDtos.isEmpty()) {
            return;
        }
        QMember qMember = QMember.member;

        List<String> inHaneMember = haneResponseDtos.stream()
                .filter(dto -> "IN".equals(dto.getInoutState()))
                .map(HaneResponseDto::getLogin)
                .toList();

        List<String> outHaneMember = haneResponseDtos.stream()
                .filter(dto -> "OUT".equals(dto.getInoutState()))
                .map(HaneResponseDto::getLogin)
                .toList();

        // bulk update 실행
        long inMemberUpdatedCount = jpaQueryFactory
                .update(qMember)
                .set(qMember.inCluster, true)
                .set(qMember.inClusterUpdatedAt, LocalDateTime.now())
                .where(qMember.intraName.in(inHaneMember))
                .execute();

        long outMemberUpdatedCount = jpaQueryFactory
                .update(qMember)
                .set(qMember.inCluster, false)
                .set(qMember.inClusterUpdatedAt, LocalDateTime.now())
                .where(qMember.intraName.in(outHaneMember))
                .execute();

        entityManager.flush();
        entityManager.clear();

        System.out.println("[hane] : " + (inMemberUpdatedCount + outMemberUpdatedCount) + "명의 inCluster 상태가 업데이트되었습니다.");
    }
}
