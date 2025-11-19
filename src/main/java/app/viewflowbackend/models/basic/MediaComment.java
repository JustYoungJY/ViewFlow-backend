package app.viewflowbackend.models.basic;


import app.viewflowbackend.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaComment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viewer_id")
    private Viewer viewer;

    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "content")
    private String content;

    @Column(name = "stars")
    private Integer stars;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
