package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.FavoriteMediaPK;
import app.viewflowbackend.models.binders.FavoriteMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteMediaRepository extends JpaRepository<FavoriteMedia, FavoriteMediaPK> {
    Page<FavoriteMedia> findByViewerId(Long id, Pageable pageable);
}
