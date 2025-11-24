package app.viewflowbackend.repositories;

import app.viewflowbackend.id.CompilationLikePK;
import app.viewflowbackend.models.binders.CompilationLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationLikeRepository extends JpaRepository<CompilationLike, CompilationLikePK> {

    boolean existsById(CompilationLikePK compilationLikePK);

    void deleteById(CompilationLikePK compilationLikePK);
}
