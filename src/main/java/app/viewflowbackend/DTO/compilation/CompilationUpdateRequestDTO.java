package app.viewflowbackend.DTO.compilation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdateRequestDTO {

    private String title;

    private String description;

    private Boolean isPublic;
}
