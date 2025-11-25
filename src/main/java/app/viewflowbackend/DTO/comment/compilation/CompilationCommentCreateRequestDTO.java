package app.viewflowbackend.DTO.comment.compilation;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCommentCreateRequestDTO {

    @NotBlank(message = "Комментарий должен быть длиной от 1 до 512 символов")
    @Size(min = 1, max = 512, message = "Комментарий должен быть длиной от 1 до 512 символов")
    private String content;

    @NotBlank(message = "Id подборки не может быть пустым")
    private Long compilationId;
}
