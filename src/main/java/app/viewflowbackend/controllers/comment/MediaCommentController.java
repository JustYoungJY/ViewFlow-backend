package app.viewflowbackend.controllers.comment;

import app.viewflowbackend.DTO.comment.media.MediaCommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.media.MediaCommentResponseDTO;
import app.viewflowbackend.DTO.comment.media.MediaCommentUpdateRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.comment.MediaCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/media")
public class MediaCommentController {

    private final MediaCommentService mediaCommentService;

    @Autowired
    public MediaCommentController(MediaCommentService mediaCommentService) {
        this.mediaCommentService = mediaCommentService;
    }


    @PostMapping
    public ResponseEntity<Void> createComment(@CurrentUser Viewer viewer,
                                              @Valid @RequestBody MediaCommentCreateRequestDTO dto) {
        mediaCommentService.createComment(viewer, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{mediaId}/{mediaType}")
    public ResponseEntity<Page<MediaCommentResponseDTO>> getMediaComments(@PathVariable Long mediaId,
                                                                          @PathVariable MediaType mediaType,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(mediaCommentService.getComments(mediaId, mediaType, pageable));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateMediaComment(@CurrentUser Viewer viewer, @PathVariable Long commentId,
                                                   @Valid @RequestBody MediaCommentUpdateRequestDTO dto) {
        mediaCommentService.updateComment(viewer, commentId, dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteMediaComment(@CurrentUser Viewer viewer, @PathVariable Long commentId) {
        mediaCommentService.deleteComment(viewer, commentId);
        return ResponseEntity.noContent().build();
    }
}
