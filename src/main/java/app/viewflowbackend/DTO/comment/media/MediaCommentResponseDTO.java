package app.viewflowbackend.DTO.comment.media;


import app.viewflowbackend.DTO.comment.ViewerInCommentDTO;
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
public class MediaCommentResponseDTO {

    private Long id;

    private ViewerInCommentDTO viewer;

    private String content;

    private MediaType mediaType;

    private Integer stars;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
