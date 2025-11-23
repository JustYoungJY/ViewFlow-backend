package app.viewflowbackend.DTO.watchedMedia;

import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WatchedMediaAddRequestDTO {

    private Long mediaId;

    private MediaType mediaType;

    private Integer progress;

    private Integer lastSeason;

    private Integer lastEpisode;
}
