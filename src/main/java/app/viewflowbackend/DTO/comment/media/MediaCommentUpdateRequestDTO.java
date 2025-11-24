package app.viewflowbackend.DTO.comment.media;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaCommentUpdateRequestDTO {

    private String content;

    private Integer stars;
}
