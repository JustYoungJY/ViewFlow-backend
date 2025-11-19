package app.viewflowbackend.DTO.compilationMedia;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationMediaAddRequestDTO {

    private Long mediaId;

    private MediaType mediaType;

    private Integer orderIndex;

    private String authorDescription;
}
