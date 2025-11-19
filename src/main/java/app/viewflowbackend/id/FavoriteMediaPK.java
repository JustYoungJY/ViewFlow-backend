package app.viewflowbackend.id;

import app.viewflowbackend.enums.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteMediaPK implements Serializable {

    @Column(name = "viewer_id")
    private Long viewerId;

    @Column(name = "media_id")
    private Long mediaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType;
}
