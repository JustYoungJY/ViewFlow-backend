package app.viewflowbackend.DTO.auth;


import jakarta.validation.constraints.Email;
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
public class RegisterRequestDTO {

    @NotBlank
    @Size(min = 3, max = 50, message = "username should be between 3 and 50 characters")
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "The password should be longer than 8 characters")
    private String password;
}
