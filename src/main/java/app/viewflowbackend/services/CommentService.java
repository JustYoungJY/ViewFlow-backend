package app.viewflowbackend.services;

import app.viewflowbackend.DTO.comment.CommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.CommentResponseDTO;
import app.viewflowbackend.DTO.comment.CommentUpdateRequestDTO;
import app.viewflowbackend.DTO.comment.ViewerInCommentDTO;
import app.viewflowbackend.enums.CommentType;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.AccessDeniedException;
import app.viewflowbackend.exceptions.CommentNotFoundException;
import app.viewflowbackend.exceptions.CompilationNotFoundException;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.CompilationComment;
import app.viewflowbackend.models.basic.MediaComment;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.repositories.CompilationCommentRepository;
import app.viewflowbackend.repositories.CompilationRepository;
import app.viewflowbackend.repositories.MediaCommentRepository;
import app.viewflowbackend.services.api.TmdbService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CompilationCommentRepository compilationCommentRepository;
    private final MediaCommentRepository mediaCommentRepository;
    private final CompilationRepository compilationRepository;
    private final TmdbService tmdbService;

    @Autowired
    public CommentService(CompilationCommentRepository compilationCommentRepository,
                          MediaCommentRepository mediaCommentRepository,
                          CompilationRepository compilationRepository,
                          TmdbService tmdbService
                          ) {
        this.compilationCommentRepository = compilationCommentRepository;
        this.mediaCommentRepository = mediaCommentRepository;
        this.compilationRepository = compilationRepository;
        this.tmdbService = tmdbService;
    }


    @Transactional
    public void createComment(Viewer viewer, CommentCreateRequestDTO dto) {
        if(dto.getCommentType() == CommentType.COMPILATION) {
            Compilation compilation = compilationRepository.findById(dto.getEntityId())
                    .orElseThrow(() -> new CompilationNotFoundException(dto.getEntityId()));
            CompilationComment comment = CompilationComment
                    .builder()
                    .viewer(viewer)
                    .compilation(compilation)
                    .content(dto.getContent())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            compilationCommentRepository.save(comment);
        } else if(dto.getCommentType() == CommentType.MEDIA && dto.getMediaType() != null && dto.getStars() != null) {
            tmdbService.getMediaDetails(dto.getEntityId(), dto.getMediaType());
            MediaComment comment = MediaComment
                    .builder()
                    .viewer(viewer)
                    .mediaId(dto.getEntityId())
                    .mediaType(dto.getMediaType())
                    .content(dto.getContent())
                    .stars(dto.getStars())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            mediaCommentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Invalid entity type");
        }
    }


    public Page<CommentResponseDTO> getComments(CommentType commentType, Long entityId,
                                               MediaType mediaType, Pageable pageable) {
        List<CommentResponseDTO> comments = new ArrayList<>();
        if(commentType == CommentType.COMPILATION) {
            List<CompilationComment> list = compilationCommentRepository
                    .findByIdAndIsDeletedFalse(entityId, pageable);
            comments = list.stream().map(this::mapToDTO).toList();
        } else if(commentType == CommentType.MEDIA) {
            List<MediaComment> list = mediaCommentRepository
                    .findByIdAndMediaTypeAndIsDeletedFalse(entityId, mediaType, pageable);
            comments = list.stream().map(this::mapToDTO).toList();
        }
        return new PageImpl<>(comments, pageable, comments.size());
    }


    @Transactional
    public void updateComment(Viewer viewer, Long entityId, CommentType commentType, CommentUpdateRequestDTO dto) {
        if (commentType == CommentType.COMPILATION) {
            CompilationComment compilationComment = compilationCommentRepository.findById(entityId)
                    .orElseThrow(() -> new CommentNotFoundException(entityId));

            if (!compilationComment.getViewer().getId().equals(viewer.getId())) {
                throw new AccessDeniedException("Not owner");
            }
            compilationComment.setContent(dto.getContent());
            compilationComment.setUpdatedAt(LocalDateTime.now());
            compilationCommentRepository.save(compilationComment);

        } else if(commentType == CommentType.MEDIA) {
            MediaComment mediaComment = mediaCommentRepository.findById(entityId)
                    .orElseThrow(() -> new CommentNotFoundException(entityId));
            if (!mediaComment.getViewer().getId().equals(viewer.getId())) {
                throw new AccessDeniedException("Not owner");
            }

            mediaComment.setContent(dto.getContent());
            mediaComment.setStars(dto.getStars() == null ? mediaComment.getStars() : dto.getStars());
            mediaComment.setUpdatedAt(LocalDateTime.now());
            mediaCommentRepository.save(mediaComment);
        } else {
            throw new IllegalArgumentException("Invalid entity type");
        }
    }


    @Transactional
    public void deleteComment(Viewer viewer, CommentType commentType, Long entityId) {
        if (commentType == CommentType.COMPILATION) {
            CompilationComment compilationComment = compilationCommentRepository
                    .findById(entityId).orElseThrow(() -> new CommentNotFoundException(entityId));
            if(!compilationComment.getViewer().getId().equals(viewer.getId())) {
                throw new AccessDeniedException("Not owner");
            }

            compilationComment.setIsDeleted(true);
            compilationCommentRepository.save(compilationComment);
        } else if(commentType == CommentType.MEDIA) {

            MediaComment mediaComment = mediaCommentRepository.findById(entityId)
                    .orElseThrow(() -> new CommentNotFoundException(entityId));
            if (!mediaComment.getViewer().getId().equals(viewer.getId())) {
                throw new AccessDeniedException("Not owner");
            }
            mediaComment.setIsDeleted(true);
            mediaCommentRepository.save(mediaComment);
        } else {
            throw new IllegalArgumentException("Invalid entity type");
        }
    }


    private CommentResponseDTO mapToDTO(CompilationComment comment) {
        ViewerInCommentDTO viewer = ViewerInCommentDTO
                .builder()
                .viewerId(comment.getViewer().getId())
                .username(comment.getViewer().getUsername())
                .avatarUrl(comment.getViewer().getAvatarUrl())
                .build();

        return CommentResponseDTO
                .builder()
                .id(comment.getId())
                .viewer(viewer)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    private CommentResponseDTO mapToDTO(MediaComment comment) {
        ViewerInCommentDTO viewer = ViewerInCommentDTO
                .builder()
                .viewerId(comment.getViewer().getId())
                .username(comment.getViewer().getUsername())
                .avatarUrl(comment.getViewer().getAvatarUrl())
                .build();

        return CommentResponseDTO
                .builder()
                .id(comment.getId())
                .viewer(viewer)
                .content(comment.getContent())
                .mediaType(comment.getMediaType())
                .stars(comment.getStars())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
