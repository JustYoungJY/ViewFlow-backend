package app.viewflowbackend.DTO.compilation;


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
public class CompilationUpdateRequestDTO {

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 1, max = 64, message = "Название должно быть длиной от 1 до 64 символов")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 1, max = 512, message = "Описание должно быть длиной от 1 до 512 символов")
    private String description;

    @NotBlank(message = "Настройки публичности не могут быть пустыми")
    private Boolean isPublic;
}
