package app.viewflowbackend.DTO.compilation;

import app.viewflowbackend.DTO.compilationMedia.CompilationMediaAddRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateRequestDTO {

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 1, max = 64, message = "Название должно быть длиной от 1 до 64 символов")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 1, max = 512, message = "Описание должно быть длиной от 1 до 512 символов")
    private String description;

    @NotBlank(message = "Картинка не может отсутствовать")
    private String imageUrl;

    @NotBlank(message = "Настройки публичности не могут быть пустыми")
    private Boolean isPublic;

    @Size(min = 1, message = "Подборка должно содержать хотя бы 1 фильм")
    private List<CompilationMediaAddRequestDTO> mediaList;
}
