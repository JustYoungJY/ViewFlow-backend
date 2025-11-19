package app.viewflowbackend.DTO.auxiliary;


import app.viewflowbackend.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaRefDTO {

    private Long id;

    private MediaType mediaType;
}
