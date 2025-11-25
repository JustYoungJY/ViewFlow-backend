package app.viewflowbackend.DTO.badge;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BadgeUpdateRequestDTO {

    @NotBlank(message = "Имя бейджа не должно быть пустым")
    private String name;

    @NotBlank(message = "Цвет бейджа не должен быть пустым")
    private String color;
}
