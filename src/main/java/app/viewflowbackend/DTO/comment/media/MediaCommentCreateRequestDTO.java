package app.viewflowbackend.DTO.comment.media;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaCommentCreateRequestDTO {

    private String content;

    private Long mediaId;

    private MediaType mediaType;

    private Integer stars;
}
