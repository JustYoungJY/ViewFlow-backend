package app.viewflowbackend.models.basic;


import app.viewflowbackend.models.binders.CompilationLike;
import app.viewflowbackend.models.binders.CompilationMedia;
import app.viewflowbackend.models.binders.CompilationTag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compilation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "media_count")
    private Integer mediaCount;

    @Column(name = "likes_count")
    private Integer likesCount;

    @ManyToOne
    @JoinColumn(name = "viewer_id")
    @ToString.Exclude
    private Viewer viewer;

    @OneToMany(mappedBy = "compilation")
    @ToString.Exclude
    private List<CompilationMedia> compilationMedia;

    @OneToMany(mappedBy = "compilation")
    @ToString.Exclude
    private List<CompilationLike> likes;

    @OneToMany(mappedBy = "compilation")
    @ToString.Exclude
    private List<CompilationTag> tags;

    @OneToMany(mappedBy = "compilation")
    @ToString.Exclude
    private List<CompilationComment> comments;
}
