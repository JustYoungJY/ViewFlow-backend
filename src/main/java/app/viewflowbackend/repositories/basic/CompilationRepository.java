package app.viewflowbackend.repositories.basic;

import app.viewflowbackend.models.basic.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAll(Specification<Compilation> spec, Pageable pageable);

    @Query("SELECT SUM(c.likesCount) FROM Compilation c WHERE c.viewer.id = :viewerId")
    Optional<Long> sumLikesCountByViewerId(Long viewerId);

    long countByViewerId(Long viewerId);
}
