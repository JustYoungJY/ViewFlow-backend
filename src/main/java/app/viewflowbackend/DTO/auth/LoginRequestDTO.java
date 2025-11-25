package app.viewflowbackend.DTO.auth;

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
public class LoginRequestDTO {

    @NotBlank(message = "Username должен быть длиной от 5 до 32 символов")
    @Size(min = 5, max = 32, message = "Username должен быть длиной от 5 до 32 символов")
    private String username;

    @NotBlank(message = "Пароль должен быть длиной от 8 до 32 символов")
    @Size(min = 8, max = 32, message = "Пароль должен быть длиной от 8 до 32 символов")
    private String password;

}
