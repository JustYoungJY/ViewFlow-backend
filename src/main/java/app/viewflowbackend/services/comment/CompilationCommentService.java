package app.viewflowbackend.services.comment;


import app.viewflowbackend.DTO.comment.ViewerInCommentDTO;
import app.viewflowbackend.DTO.comment.compilation.CompilationCommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.compilation.CompilationCommentResponseDTO;
import app.viewflowbackend.DTO.comment.compilation.CompilationCommentUpdateRequestDTO;
import app.viewflowbackend.exceptions.PermissionDeniedException;
import app.viewflowbackend.exceptions.CommentNotFoundException;
import app.viewflowbackend.exceptions.CompilationNotFoundException;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.CompilationComment;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.repositories.CompilationCommentRepository;
import app.viewflowbackend.repositories.CompilationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CompilationCommentService {

    private final CompilationCommentRepository compilationCommentRepository;
    private final CompilationRepository compilationRepository;


    @Autowired
    public CompilationCommentService(CompilationCommentRepository compilationCommentRepository,
                                     CompilationRepository compilationRepository) {
        this.compilationCommentRepository = compilationCommentRepository;
        this.compilationRepository = compilationRepository;
    }


    @Transactional
    public void createComment(Viewer viewer, CompilationCommentCreateRequestDTO dto) {
        Compilation compilation = compilationRepository.findById(dto.getCompilationId())
                .orElseThrow(() -> new CompilationNotFoundException(dto.getCompilationId()));
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
    }


    public Page<CompilationCommentResponseDTO> getComments(Long compilationId, Pageable pageable) {
        Page<CompilationComment> commentPage = compilationCommentRepository
                .findByCompilationIdAndIsDeletedFalse(compilationId, pageable);

        return commentPage.map(this::mapToDTO);
    }


    @Transactional
    public void updateComment(Viewer viewer, Long commentId, CompilationCommentUpdateRequestDTO dto) {

        CompilationComment compilationComment = compilationCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!compilationComment.getViewer().getId().equals(viewer.getId())) {
            throw new PermissionDeniedException("Not owner");
        }
        compilationComment.setContent(dto.getContent());
        compilationComment.setUpdatedAt(LocalDateTime.now());
        compilationCommentRepository.save(compilationComment);

    }


    @Transactional
    public void deleteComment(Viewer viewer, Long commentId) {
        CompilationComment compilationComment = compilationCommentRepository
                .findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!compilationComment.getViewer().getId().equals(viewer.getId())) {
            throw new PermissionDeniedException("Not owner");
        }

        compilationComment.setIsDeleted(true);
        compilationCommentRepository.save(compilationComment);
    }


    private CompilationCommentResponseDTO mapToDTO(CompilationComment comment) {
        ViewerInCommentDTO viewer = ViewerInCommentDTO
                .builder()
                .viewerId(comment.getViewer().getId())
                .username(comment.getViewer().getUsername())
                .avatarUrl(comment.getViewer().getAvatarUrl())
                .build();

        return CompilationCommentResponseDTO
                .builder()
                .id(comment.getId())
                .viewer(viewer)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
