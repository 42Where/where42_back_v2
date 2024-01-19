package kr.where.backend.jwt;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<JsonWebToken, Long> {
    Optional<JsonWebToken> findByIntraId(final Integer intraId);
    Optional<JsonWebToken> findByRequestIp(final String requestIp);
}
