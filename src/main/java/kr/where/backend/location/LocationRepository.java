package kr.where.backend.location;

import java.util.List;
import kr.where.backend.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	Location findByMember(Member member);

	@Query("SELECT l FROM Location l "
			+ "WHERE l.imacLocation LIKE :prefix% "
			+ "AND (l.member.inCluster = TRUE OR l.member.agree = FALSE)")
	List<Location> getFilteredLocations(@Param("prefix") String prefix);

	Integer countAllByImacLocationStartingWith(String prefix);

	Integer countAllByImacLocationIsNotNull();

	@Modifying
	@Query("UPDATE Location location SET location.imacLocation = null")
	void setNullImacOfLocation();

}
