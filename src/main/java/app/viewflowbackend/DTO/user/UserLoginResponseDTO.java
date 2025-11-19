package app.viewflowbackend.DTO.user;

import app.viewflowbackend.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {

    private String JWT;

    private String username;

    private Role role;
}
