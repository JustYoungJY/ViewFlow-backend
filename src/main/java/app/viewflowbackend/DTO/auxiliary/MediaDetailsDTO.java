package app.viewflowbackend.DTO.auxiliary;

import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * A class that stores data about a movie/TV series.
 * It is cached and retrieved from a third-party API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDetailsDTO {
    private Integer tmdbId;

    private MediaType mediaType;

    private String title;

    private String originalTitle;

    private String posterPath;

    private String backdropPath;

    private Integer releaseYear;

    private String overview;

    private Double voteAverage;

    private Integer voteCount;

    private String trailerYoutubeId;

    private List<String> genres;

    private Integer runtime;

    private Integer numberOfSeasons;

}
