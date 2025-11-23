package app.viewflowbackend.repositories;

import app.viewflowbackend.models.basic.CompilationComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationCommentRepository extends JpaRepository<CompilationComment, Long> {
    List<CompilationComment> findByIdAndIsDeletedFalse(Long entityId, Pageable pageable);
}
