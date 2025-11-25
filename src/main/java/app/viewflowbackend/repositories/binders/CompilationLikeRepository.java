package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.CompilationLikePK;
import app.viewflowbackend.models.binders.CompilationLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationLikeRepository extends JpaRepository<CompilationLike, CompilationLikePK> {

    boolean existsById(CompilationLikePK compilationLikePK);

    void deleteById(CompilationLikePK compilationLikePK);

    Page<CompilationLike> findByViewerId(Long id, Pageable pageable);
}
