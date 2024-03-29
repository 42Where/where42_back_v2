package kr.where.backend.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByIntraId(Integer intraId);

	Optional<List<Member>> findByIntraIdIn(List<Integer> intraId);

	Optional<List<Member>> findAllByAgreeTrue();
}
