package app.viewflowbackend.DTO.category;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCardResponseDTO {

    private Integer id;

    private String name;

    private String description;

    private String iconUrl;
}
