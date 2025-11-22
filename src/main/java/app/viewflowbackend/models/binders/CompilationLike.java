package app.viewflowbackend.models.binders;


import app.viewflowbackend.id.CompilationLikePK;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.Viewer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "compilation_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationLike {

    @EmbeddedId
    private CompilationLikePK id;

    @MapsId("compilationId")
    @ManyToOne
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @MapsId("viewerId")
    @ManyToOne
    @JoinColumn(name = "viewer_id")
    private Viewer viewer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
