package app.viewflowbackend.DTO.compilationMedia;


import app.viewflowbackend.enums.MediaType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationMediaAddRequestDTO {

    @NotNull(message = "Id медиа не может быть пустым")
    private Long mediaId;

    @NotNull(message = "Тип медиа не может быть пустым")
    private MediaType mediaType;

    @NotNull(message = "Порядковый номер не может быть пустым")
    private Integer orderIndex;

    private String authorDescription;
}
