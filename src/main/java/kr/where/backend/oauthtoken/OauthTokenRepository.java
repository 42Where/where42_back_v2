package kr.where.backend.oauthtoken;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {
    Optional<OauthToken> findByName(String name);
}
