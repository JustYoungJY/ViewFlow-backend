package app.viewflowbackend.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationLikePK implements Serializable {

    @Column(name = "compilation_id")
    private Long compilationId;

    @Column(name = "viewer_id")
    private Long viewerId;
}
