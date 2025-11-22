package app.viewflowbackend.DTO.compilation;

import app.viewflowbackend.DTO.compilationMedia.CompilationMediaAddRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateRequestDTO {

    private String title;

    private String description;

    private String imageUrl;

    private Boolean isPublic;

    private List<CompilationMediaAddRequestDTO> mediaList;
}
