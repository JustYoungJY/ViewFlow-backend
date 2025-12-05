package app.viewflowbackend.models.binders;

import app.viewflowbackend.id.MediaBadgePK;
import app.viewflowbackend.models.basic.Badge;
import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    private Badge badge;
}
