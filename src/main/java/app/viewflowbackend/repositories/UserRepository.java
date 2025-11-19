package app.viewflowbackend.repositories;

import app.viewflowbackend.models.basic.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Viewer, Long> {
    Optional<Viewer> findByUsername(String username);
    Optional<Viewer> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
