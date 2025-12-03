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
public class CompilationMediaUpdateRequestDTO {

    @NotNull
    private Long mediaId;

    @NotNull
    private MediaType mediaType;

    private String authorDescription;

    private Integer orderIndex;
}