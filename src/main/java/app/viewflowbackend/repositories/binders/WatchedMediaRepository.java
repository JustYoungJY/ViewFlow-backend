package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.id.WatchedMediaPK;
import app.viewflowbackend.models.binders.WatchedMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchedMediaRepository extends JpaRepository<WatchedMedia, WatchedMediaPK> {
    Page<WatchedMedia> findByViewerIdAndId_MediaType(Long id, MediaType mediaType, Pageable pageable);

    Page<WatchedMedia> findByViewerId(Long id, Pageable pageable);

    long countByViewerId(Long viewerId);
}
