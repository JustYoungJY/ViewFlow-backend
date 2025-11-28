package app.viewflowbackend.DTO.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaRatingResponseDTO {

    private Double ratingKinopoisk = 0.0;

    private Double ratingImdb = 0.0;

    private Double ratingTmdb = 0.0;
}
