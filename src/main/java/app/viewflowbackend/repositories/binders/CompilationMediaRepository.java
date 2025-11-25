package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.CompilationMediaPK;
import app.viewflowbackend.models.binders.CompilationMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationMediaRepository extends JpaRepository<CompilationMedia, CompilationMediaPK> {
    List<CompilationMedia> findByCompilationId(Long compilation_id);

    void deleteById(CompilationMediaPK compilationMediaPK);
}
