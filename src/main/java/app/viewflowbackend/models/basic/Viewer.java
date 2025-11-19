package app.viewflowbackend.models.basic;


import app.viewflowbackend.enums.Role;
import app.viewflowbackend.models.binders.CompilationLike;
import app.viewflowbackend.models.binders.FavoriteMedia;
import app.viewflowbackend.models.binders.WatchedMedia;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "viewer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Viewer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viewer_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "viewer")
    private List<Compilation> compilations;

    @OneToMany(mappedBy = "viewer")
    private List<CompilationComment> compilationComments;

    @OneToMany(mappedBy = "viewer")
    private List<FavoriteMedia> favoriteMedia;

    @OneToMany(mappedBy = "viewer")
    private List<MediaComment> mediaComments;

    @OneToMany(mappedBy = "viewer")
    private List<WatchedMedia> watchedMedia;

    @OneToMany(mappedBy = "viewer")
    private List<CompilationLike> compilationLikes;
}
