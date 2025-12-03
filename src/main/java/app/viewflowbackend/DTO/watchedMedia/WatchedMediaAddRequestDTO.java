package app.viewflowbackend.DTO.watchedMedia;

import app.viewflowbackend.enums.MediaType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WatchedMediaAddRequestDTO {

    @NotNull(message = "Id медиа не может быть пустым")
    private Long mediaId;

    @NotNull(message = "Тип медиа не может быть пустым")
    private MediaType mediaType;

    @NotNull(message = "Прогресс не может быть пустым")
    @Min(value = 0, message = "Прогресс должен быть больше или равен 0")
    @Max(value = 100, message = "Прогресс должен быть меньше или равен 100")
    private Integer progress;

    private Integer lastSeason;

    private Integer lastEpisode;
}
