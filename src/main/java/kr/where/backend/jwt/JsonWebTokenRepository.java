package kr.where.backend.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonWebTokenRepository extends JpaRepository<JsonWebToken, Long> {

}
