package app.viewflowbackend.models.binders;


import app.viewflowbackend.id.CompilationTagPK;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "compilation_tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationTag {

    @EmbeddedId
    CompilationTagPK id;

    @MapsId("tagId")
    @ManyToOne
    @JoinColumn(name = "tag_id")
    @ToString.Exclude
    private Tag tag;

    @MapsId("compilationId")
    @ManyToOne
    @JoinColumn(name = "compilation_id")
    @ToString.Exclude
    private Compilation compilation;

}
