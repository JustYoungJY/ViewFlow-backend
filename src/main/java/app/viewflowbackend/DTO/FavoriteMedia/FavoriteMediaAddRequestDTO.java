package app.viewflowbackend.DTO.FavoriteMedia;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteMediaAddRequestDTO {

    private Long mediaId;

    private MediaType mediaType;
}
