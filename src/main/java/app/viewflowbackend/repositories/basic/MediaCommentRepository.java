package app.viewflowbackend.repositories.basic;

import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.MediaComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaCommentRepository extends JpaRepository<MediaComment, Long> {
    Page<MediaComment> findByMediaIdAndMediaTypeAndIsDeletedFalse(Long mediaId, MediaType mediaType, Pageable pageable);
}
