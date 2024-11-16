package kr.where.backend.location;

import kr.where.backend.aspect.QueryLog;
import kr.where.backend.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	@QueryLog
	Location findByMember(Member member);
}
