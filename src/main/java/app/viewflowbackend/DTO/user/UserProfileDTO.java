package app.viewflowbackend.DTO.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private Long id;

    private String username;

    private String email;

    private String avatarUrl;

    private String bio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Integer likesCount;

    private Integer compilationsCount;

    private Integer filmsWatched;


}
