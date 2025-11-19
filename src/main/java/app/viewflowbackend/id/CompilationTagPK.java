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
public class CompilationTagPK implements Serializable {

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "compilation_id")
    private Long compilationId;
}
