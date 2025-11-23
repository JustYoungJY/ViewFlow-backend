package app.viewflowbackend.DTO.comment;


import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
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
public class CommentResponseDTO {

    private Long id;

    private ViewerInCommentDTO viewer;

    private String content;

    // Maybe null if it`s compilation
    private MediaType mediaType;

    // Maybe null if it`s compilation
    private Integer stars;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
