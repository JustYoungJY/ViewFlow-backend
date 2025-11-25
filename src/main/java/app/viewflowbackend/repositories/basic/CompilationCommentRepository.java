package app.viewflowbackend.repositories.basic;

import app.viewflowbackend.models.basic.CompilationComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationCommentRepository extends JpaRepository<CompilationComment, Long> {
    Page<CompilationComment> findByCompilationIdAndIsDeletedFalse(Long compilationId, Pageable pageable);
}
