package app.viewflowbackend.DTO.compilation;


import app.viewflowbackend.DTO.tag.TagResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private String viewerAvatarUrl;

    private List<TagResponseDTO> tags;

}
