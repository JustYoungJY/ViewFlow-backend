package app.viewflowbackend.DTO.comment;


import app.viewflowbackend.enums.CommentType;
import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDTO {

    private String content;

    private CommentType commentType;

    private Long entityId;

    // Maybe null if it`s compilation
    private MediaType mediaType;

    // Maybe null if it`s compilation
    private Integer stars;
}
