package app.viewflowbackend.DTO.comment.compilation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCommentUpdateRequestDTO {

    private String content;

}
