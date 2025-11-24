package app.viewflowbackend.controllers.comment;

import app.viewflowbackend.DTO.comment.compilation.CompilationCommentCreateRequestDTO;
import app.viewflowbackend.DTO.comment.compilation.CompilationCommentResponseDTO;
import app.viewflowbackend.DTO.comment.compilation.CompilationCommentUpdateRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.comment.CompilationCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/compilations")
public class CompilationCommentController {

    private final CompilationCommentService compilationCommentService;

    @Autowired
    public CompilationCommentController(CompilationCommentService compilationCommentService) {
        this.compilationCommentService = compilationCommentService;
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@CurrentUser Viewer viewer,
                                              @Valid @RequestBody CompilationCommentCreateRequestDTO dto) {
        compilationCommentService.createComment(viewer, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{compilationId}")
    public ResponseEntity<Page<CompilationCommentResponseDTO>> getCompilationComments(@PathVariable Long compilationId,
                                                                                      Pageable pageable) {
        return ResponseEntity.ok(compilationCommentService.getComments(compilationId, pageable));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateCompilationComment(@CurrentUser Viewer viewer, @PathVariable Long commentId,
                                                         @Valid @RequestBody CompilationCommentUpdateRequestDTO dto) {
        compilationCommentService.updateComment(viewer, commentId, dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCompilationComment(@CurrentUser Viewer viewer, @PathVariable Long commentId) {
        compilationCommentService.deleteComment(viewer, commentId);
        return ResponseEntity.noContent().build();
    }
}
