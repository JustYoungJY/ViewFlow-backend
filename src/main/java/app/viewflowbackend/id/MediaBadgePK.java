package app.viewflowbackend.id;


import app.viewflowbackend.enums.MediaType;
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
public class MediaBadgePK implements Serializable {

    private Long mediaId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

}
