package kr.where.backend.member;

import jakarta.persistence.LockModeType;
import kr.where.backend.api.json.hane.HaneRequestDto;
import kr.where.backend.aspect.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByIntraId(Integer intraId);

	@QueryLog
	Optional<List<Member>> findByIntraIdIn(List<Integer> intraId);

	@QueryLog
	Optional<Member> findByIntraName(String intraName);

	@QueryLog
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "select m from Member m where m.intraName in :intraNames")
	List<Member> findAllByIntraNameIn(@Param("intraNames") List<String> IntraName);

	@QueryLog
	@Query("select new kr.where.backend.api.json.hane.HaneRequestDto(m.intraName) "
			+ "from Member m where m.agree = true")
	Optional<List<HaneRequestDto>> findAllToUseHaneApi();
}
