package app.viewflowbackend.DTO.compilation;


import app.viewflowbackend.DTO.auxiliary.CompilationMediaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Integer mediaCount;

    private Integer likesCount;

    private Long viewerId;

    private List<String> tags;

    private List<CompilationMediaDTO> media;

}
