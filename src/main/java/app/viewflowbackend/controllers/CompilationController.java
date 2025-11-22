package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.compilation.CompilationCreateRequestDTO;
import app.viewflowbackend.DTO.compilation.CompilationListItemDTO;
import app.viewflowbackend.DTO.compilation.CompilationResponseDTO;
import app.viewflowbackend.DTO.compilation.CompilationUpdateRequestDTO;
import app.viewflowbackend.DTO.compilationMedia.CompilationMediaAddRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.CompilationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }


    @PostMapping
    public ResponseEntity<Void> createCompilation(@CurrentUser Viewer viewer,
                                                  @Valid @RequestBody CompilationCreateRequestDTO request) {
        compilationService.createCompilation(viewer, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompilationResponseDTO> getCompilation(@PathVariable Long id) {
        return ResponseEntity.ok(compilationService.getCompilation(id));
    }

    @GetMapping
    public ResponseEntity<Page<CompilationListItemDTO>> getCompilations(Pageable pageable,
                                                                        @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(compilationService.getCompilations(pageable, filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompilation(@CurrentUser Viewer viewer, @PathVariable Long id,
                                                  @Valid @RequestBody CompilationUpdateRequestDTO dto) throws AccessDeniedException {
        compilationService.updateCompilation(viewer, id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompilation(@CurrentUser Viewer viewer, @PathVariable Long id) throws AccessDeniedException {
        compilationService.deleteCompilation(viewer, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<Void> addMediaToCompilation(@CurrentUser Viewer viewer, @PathVariable Long id,
                                                      @Valid @RequestBody CompilationMediaAddRequestDTO dto) throws AccessDeniedException {
        compilationService.addMediaToCompilation(viewer, id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/media")
    public ResponseEntity<Void> removeMediaFromCompilation(@CurrentUser Viewer viewer, @PathVariable Long id,
                                                           @RequestParam Long mediaId, @RequestParam MediaType mediaType) throws AccessDeniedException {
        compilationService.removeMediaFromCompilation(viewer, id, mediaId, mediaType);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeCompilation(@CurrentUser Viewer viewer, @PathVariable Long id) throws AccessDeniedException {
        compilationService.likeCompilation(viewer, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeCompilation(@CurrentUser Viewer viewer, @PathVariable Long id) {
        compilationService.unlikeCompilation(viewer, id);
        return ResponseEntity.ok().build();
    }
}
