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
public class RandomMediaResponseCardDTO {

    private Long mediaId;

    private MediaType mediaType;

    private String title;

    private String description;

    private Integer year;

    private Double rating;

    private String posterUrl;

    private List<String> genres;
}
