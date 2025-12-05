package app.viewflowbackend.models.basic;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "compilation_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viewer_id")
    @ToString.Exclude
    private Viewer viewer;

    @ManyToOne
    @JoinColumn(name = "compilation_id")
    @ToString.Exclude
    private Compilation compilation;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
