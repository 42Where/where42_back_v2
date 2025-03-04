package kr.where.backend.member;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void updateMemberInOrOutStatus(Map<String, String> intraNameStateMap) {
        if (intraNameStateMap.isEmpty()) {
            return;
        }
        QMember qMember = QMember.member;

        long updatedCount = jpaQueryFactory
                .update(qMember)
                .set(qMember.inCluster,
                        new CaseBuilder()
                                .when(qMember.intraName.eq(qMember.intraName)) // 이 부분은 기본 비교로, 이미 where 절에서 필터링 했으므로, 직접 비교만 해주면 됩니다.
                                .then(intraNameStateMap.get(qMember.intraName).equals("IN")) // Map에서 "IN"이면 true, "OUT"이면 false
                                .otherwise(false))  // 기본적으로 false
                .where(qMember.intraName.in(intraNameStateMap.keySet()))  // intraName이 Map에 존재하는 경우만 업데이트
                .execute();

        entityManager.flush();
        entityManager.clear();

        System.out.println("[hane] : " + updatedCount + "명의 inCluster 상태가 업데이트되었습니다.");
    }
}
