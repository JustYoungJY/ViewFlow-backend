package app.viewflowbackend.DTO.favoriteMedia;


import app.viewflowbackend.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteMediaAddRequestDTO {

    @NotBlank(message = "Id медиа не может быть пустым")
    private Long mediaId;

    @NotBlank(message = "Тип медиа не может быть пустым")
    private MediaType mediaType;
}
