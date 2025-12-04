package app.viewflowbackend.models.binders;


import app.viewflowbackend.id.WatchedMediaPK;
import app.viewflowbackend.models.basic.Viewer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "Watched_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchedMedia {

    @EmbeddedId
    private WatchedMediaPK id;

    @MapsId("viewerId")
    @ManyToOne
    @JoinColumn(name = "viewer_id")
    @ToString.Exclude
    private Viewer viewer;

    @Column(name = "watched_at")
    private LocalDateTime watchedAt;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "last_season")
    private Integer lastSeason;

    @Column(name = "last_episode")
    private Integer lastEpisode;
}
