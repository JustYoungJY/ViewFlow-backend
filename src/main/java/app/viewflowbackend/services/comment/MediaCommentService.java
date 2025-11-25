package app.viewflowbackend.services.comment;

import app.viewflowbackend.DTO.comment.ViewerInCommentDTO;
import app.viewflowbackend.DTO.comment.media.MediaCommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.media.MediaCommentResponseDTO;
import app.viewflowbackend.DTO.comment.media.MediaCommentUpdateRequestDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.auth.PermissionDeniedException;
import app.viewflowbackend.exceptions.notFound.CommentNotFoundException;
import app.viewflowbackend.models.basic.MediaComment;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.repositories.basic.MediaCommentRepository;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class MediaCommentService {


    private final MediaCommentRepository mediaCommentRepository;
    private final TmdbService tmdbService;

    @Autowired
    public MediaCommentService(MediaCommentRepository mediaCommentRepository,
                               TmdbService tmdbService
    ) {
        this.mediaCommentRepository = mediaCommentRepository;
        this.tmdbService = tmdbService;
    }


    @Transactional
    public void createComment(Viewer viewer, MediaCommentCreateRequestDTO dto) {
        tmdbService.checkMediaExists(dto.getMediaId(), dto.getMediaType());
        MediaComment comment = MediaComment
                .builder()
                .viewer(viewer)
                .mediaId(dto.getMediaId())
                .mediaType(dto.getMediaType())
                .content(dto.getContent())
                .stars(dto.getStars())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        mediaCommentRepository.save(comment);
    }


    public Page<MediaCommentResponseDTO> getComments(Long entityId,
                                                     MediaType mediaType, Pageable pageable) {
        Page<MediaComment> commentPage = mediaCommentRepository
                .findByMediaIdAndMediaTypeAndIsDeletedFalse(entityId, mediaType, pageable);

        return commentPage.map(this::mapToDTO);
    }


    @Transactional
    public void updateComment(Viewer viewer, Long entityId, MediaCommentUpdateRequestDTO dto) {
        MediaComment mediaComment = mediaCommentRepository.findById(entityId)
                .orElseThrow(() -> new CommentNotFoundException(entityId));
        if (!mediaComment.getViewer().getId().equals(viewer.getId())) {
            throw new PermissionDeniedException("Not owner");
        }

        mediaComment.setContent(dto.getContent());
        mediaComment.setStars(dto.getStars() == null ? mediaComment.getStars() : dto.getStars());
        mediaComment.setUpdatedAt(LocalDateTime.now());
        mediaCommentRepository.save(mediaComment);
    }


    @Transactional
    public void deleteComment(Viewer viewer, Long entityId) {
        MediaComment mediaComment = mediaCommentRepository.findById(entityId)
                .orElseThrow(() -> new CommentNotFoundException(entityId));
        if (!mediaComment.getViewer().getId().equals(viewer.getId())) {
            throw new PermissionDeniedException("Not owner");
        }
        mediaComment.setIsDeleted(true);
        mediaCommentRepository.save(mediaComment);
    }


    private MediaCommentResponseDTO mapToDTO(MediaComment comment) {
        ViewerInCommentDTO viewer = ViewerInCommentDTO
                .builder()
                .viewerId(comment.getViewer().getId())
                .username(comment.getViewer().getUsername())
                .avatarUrl(comment.getViewer().getAvatarUrl())
                .build();

        return MediaCommentResponseDTO
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
