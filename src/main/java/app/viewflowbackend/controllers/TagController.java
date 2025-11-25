package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.tag.TagCreateRequest;
import app.viewflowbackend.DTO.tag.TagResponseDTO;
import app.viewflowbackend.DTO.tag.TagUpdateRequest;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;


    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createTag(@Valid @RequestBody TagCreateRequest tagCreateRequest) {
        tagService.createTag(tagCreateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }


    @GetMapping
    public ResponseEntity<Page<TagResponseDTO>> getTags(@RequestParam(required = false) String query, Pageable pageable) {
        return ResponseEntity.ok(tagService.getTags(query, pageable));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTag(@PathVariable Long id, @Valid @RequestBody TagUpdateRequest tagUpdateRequest) {
        tagService.updateTag(id, tagUpdateRequest);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/compilations/{compilationId}")
    public ResponseEntity<Void> addTagToCompilation(@CurrentUser Viewer viewer, @PathVariable Long compilationId,
                                                    @RequestParam Long tagId) {
        tagService.addTagToCompilation(viewer, compilationId, tagId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/compilations/{compilationId}/{tagId}")
    public ResponseEntity<Void> removeTagFromCompilation(@CurrentUser Viewer viewer, @PathVariable Long compilationId,
                                                         @PathVariable Long tagId) {
        tagService.removeTagFromCompilation(viewer, compilationId, tagId);
        return ResponseEntity.ok().build();
    }
}
