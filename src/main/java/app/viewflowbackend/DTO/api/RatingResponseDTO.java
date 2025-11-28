package app.viewflowbackend.DTO.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDTO {

    private Double ratingKinopoisk;

    private Double ratingImdb;
}
