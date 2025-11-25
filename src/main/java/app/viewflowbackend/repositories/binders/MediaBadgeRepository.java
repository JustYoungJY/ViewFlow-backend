package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.MediaBadgePK;
import app.viewflowbackend.models.binders.MediaBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaBadgeRepository extends JpaRepository<MediaBadge, MediaBadgePK> {
}
