package app.viewflowbackend.models.binders;

import app.viewflowbackend.id.MediaBadgePK;
import app.viewflowbackend.models.basic.Badge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "media_badge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaBadge {

    @EmbeddedId
    private MediaBadgePK id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;
}
