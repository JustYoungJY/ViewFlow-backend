package app.viewflowbackend.models.binders;


import app.viewflowbackend.id.FavoriteMediaPK;
import app.viewflowbackend.models.basic.Viewer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteMedia {

    @EmbeddedId
    private FavoriteMediaPK id;

    @ManyToOne
    @MapsId("viewerId")
    @JoinColumn(name = "viewer_id")
    private Viewer viewer;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

}
