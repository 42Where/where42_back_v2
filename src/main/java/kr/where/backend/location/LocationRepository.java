package kr.where.backend.location;

import java.util.List;
import kr.where.backend.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	Location findByMember(Member member);

	List<Location> findByImacLocationStartingWith(String prefix);

	Integer countAllByImacLocationStartingWith(String prefix);

	Integer countAllByImacLocationIsNotNull();
}
