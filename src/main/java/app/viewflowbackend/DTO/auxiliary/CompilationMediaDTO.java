package app.viewflowbackend.DTO.auxiliary;


import app.viewflowbackend.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationMediaDTO {
    private Long mediaId;

    private MediaType mediaType;

    private Integer orderIndex;

    private String authorDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addedAt;

    // Cache data
    private MediaDetailsDTO mediaDetails;
}
