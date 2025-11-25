package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.CompilationTagPK;
import app.viewflowbackend.models.binders.CompilationTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationTagRepository extends JpaRepository<CompilationTag, CompilationTagPK> {
}
