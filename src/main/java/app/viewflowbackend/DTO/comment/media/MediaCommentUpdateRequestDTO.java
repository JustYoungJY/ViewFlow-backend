package app.viewflowbackend.DTO.comment.media;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class MediaCommentUpdateRequestDTO {

    @NotBlank(message = "Комментарий должен быть длиной от 1 до 512 символов")
    @Size(min = 1, max = 512, message = "Комментарий должен быть длиной от 1 до 512 символов")
    private String content;

    @NotBlank(message = "Количество звезд не может быть пустым")
    @Min(value = 1, message = "Количество звезд должно быть больше или равно 1")
    @Max(value = 10, message = "Количество звезд должно быть меньше или равно 10")
    private Integer stars;
}
