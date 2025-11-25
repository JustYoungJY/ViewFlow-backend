package app.viewflowbackend.DTO.user;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {

    private String avatarUrl;

    @Size(min = 1, max = 255, message = "Био должно быть длиной от 1 до 255 символов")
    private String bio;
}
