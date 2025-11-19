package app.viewflowbackend.DTO.compilation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationListItemDTO {

    private Long id;

    private String title;

    private String imageUrl;

    private Integer mediaCount;

    private Integer likesCount;

    private String viewerUsername;

}
