package com.interviewcoach.repo;

import com.interviewcoach.model.AuthToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthTokenRepo extends JpaRepository<AuthToken, Long> {
    // Load the associated User before the security filter accesses its id.
    // The original direct field access on a lazy Hibernate proxy can produce null.
    @EntityGraph(attributePaths = "user")
    Optional<AuthToken> findByTokenHash(String hash);
    void deleteByTokenHash(String hash);
}
