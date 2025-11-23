package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.comment.CommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.CommentResponseDTO;
import app.viewflowbackend.DTO.comment.CommentUpdateRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.CommentType;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@CurrentUser Viewer viewer,
                                              @Valid @RequestBody CommentCreateRequestDTO dto) {
        commentService.createComment(viewer, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/compilation/{commentId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCompilationComments(@PathVariable Long commentId,
                                                                           Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(CommentType.COMPILATION, commentId, null, pageable));
    }

    @GetMapping("/media/{commentId}/{mediaType}")
    public ResponseEntity<Page<CommentResponseDTO>> getMediaComments(@PathVariable Long commentId,
                                                                     @PathVariable MediaType mediaType,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(CommentType.MEDIA, commentId, mediaType, pageable));
    }

    @PutMapping("/compilation/{commentId}")
    public ResponseEntity<Void> updateCompilationComment(@CurrentUser Viewer viewer, @PathVariable Long commentId,
                                              @Valid @RequestBody CommentUpdateRequestDTO dto) {
        commentService.updateComment(viewer, commentId, CommentType.COMPILATION, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/media/{commentId}")
    public ResponseEntity<Void> updateMediaComment(@CurrentUser Viewer viewer, @PathVariable Long commentId,
                                              @Valid @RequestBody CommentUpdateRequestDTO dto) {
        commentService.updateComment(viewer, commentId, CommentType.MEDIA , dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compilation/{commentId}")
    public ResponseEntity<Void> deleteCompilationComment(@CurrentUser Viewer viewer, @PathVariable Long commentId) {
        commentService.deleteComment(viewer, CommentType.COMPILATION, commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/media/{commentId}")
    public ResponseEntity<Void> deleteMediaComment(@CurrentUser Viewer viewer, @PathVariable Long commentId) {
        commentService.deleteComment(viewer, CommentType.MEDIA, commentId);
        return ResponseEntity.noContent().build();
    }
}
