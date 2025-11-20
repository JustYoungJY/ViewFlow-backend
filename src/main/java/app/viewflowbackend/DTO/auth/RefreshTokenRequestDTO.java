package app.viewflowbackend.DTO.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {

    @NotBlank
    String refreshToken;
}
