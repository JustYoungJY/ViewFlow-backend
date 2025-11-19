package app.viewflowbackend.DTO.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {

    private String username;

    private String avatarUrl;

    private String bio;
}
