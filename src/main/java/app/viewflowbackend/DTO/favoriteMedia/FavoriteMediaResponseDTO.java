package app.viewflowbackend.DTO.favoriteMedia;


import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
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
public class FavoriteMediaResponseDTO {

    private Long mediaId;

    private MediaType mediaType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addedAt;

    private MediaDetailsDTO mediaDetails;
}
