package app.viewflowbackend.DTO.badge;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BadgeUpdateRequestDTO {

    private String name;

    private String color;
}
