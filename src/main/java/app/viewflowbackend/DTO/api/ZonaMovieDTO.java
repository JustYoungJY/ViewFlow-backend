package app.viewflowbackend.DTO.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonaMovieDTO {
    private String title;
    private int year;
    private String fullUrl;
}