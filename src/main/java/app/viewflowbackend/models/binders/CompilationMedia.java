package app.viewflowbackend.models.binders;


import app.viewflowbackend.id.CompilationMediaPK;
import app.viewflowbackend.models.basic.Compilation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "compilation_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationMedia {

    @EmbeddedId
    private CompilationMediaPK id;

    @ManyToOne
    @MapsId("compilationId")
    @JoinColumn(name = "compilation_id")
    @ToString.Exclude
    private Compilation compilation;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "author_description")
    private String authorDescription;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}
