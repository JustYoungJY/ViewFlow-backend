package app.viewflowbackend.DTO.mediaRating;


import app.viewflowbackend.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaRatingResponseDTO {
    private Long viewerId;

    private Long mediaId;

    private MediaType mediaType;

    private Integer stars;

    private String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
