package app.viewflowbackend.repositories;

import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.MediaComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaCommentRepository extends JpaRepository<MediaComment, Long> {
    List<MediaComment> findByIdAndMediaTypeAndIsDeletedFalse(Long entityId, MediaType mediaType, Pageable pageable);
}
