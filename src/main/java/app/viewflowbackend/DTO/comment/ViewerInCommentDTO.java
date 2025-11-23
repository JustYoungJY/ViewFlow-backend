package app.viewflowbackend.DTO.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewerInCommentDTO {

    private Long viewerId;

    private String username;

    private String avatarUrl;
}
