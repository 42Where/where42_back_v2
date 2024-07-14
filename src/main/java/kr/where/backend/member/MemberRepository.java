package kr.where.backend.member;

import kr.where.backend.api.json.hane.HaneRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByIntraId(Integer intraId);

	Optional<List<Member>> findByIntraIdIn(List<Integer> intraId);

	Optional<List<Member>> findAllByAgreeTrue();

	@Query("select new kr.where.backend.api.json.hane.HaneRequestDto(m.intraName) "
			+ "from Member m where m.agree = true ")
	Optional<List<HaneRequestDto>> findAllToUseHaneApi();
}
