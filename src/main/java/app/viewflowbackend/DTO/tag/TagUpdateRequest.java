package app.viewflowbackend.DTO.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateRequest {

    @NotBlank(message = "Название не может быть пустым")
    private String name;
}
