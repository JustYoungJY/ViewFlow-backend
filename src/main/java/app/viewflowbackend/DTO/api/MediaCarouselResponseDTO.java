package app.viewflowbackend.DTO.api;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaCarouselResponseDTO {

    private Long mediaId;

    private MediaType mediaType;

    private String title;

    private String description;

    private String posterUrl;

}
