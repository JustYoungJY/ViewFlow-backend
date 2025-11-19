package app.viewflowbackend.DTO.MediaRating;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaRatingRequestDTO {

    private Long mediaId;

    private MediaType mediaType;

    private Integer stars;

    private String comment;
}
