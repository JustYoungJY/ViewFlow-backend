package app.viewflowbackend.controllers;


import app.viewflowbackend.DTO.watchedMedia.WatchedMediaAddRequestDTO;
import app.viewflowbackend.DTO.watchedMedia.WatchedMediaResponseDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.WatchedMediaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watched")
public class WatchedMediaController {

    private final WatchedMediaService watchedMediaService;


    @Autowired
    public WatchedMediaController(WatchedMediaService watchedMediaService) {
        this.watchedMediaService = watchedMediaService;
    }


    @PostMapping
    public ResponseEntity<Void> addOrUpdateWatched(@CurrentUser Viewer viewer,
                                                   @Valid @RequestBody WatchedMediaAddRequestDTO dto) {
        watchedMediaService.addOrUpdateWatched(viewer, dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<Page<WatchedMediaResponseDTO>> getWatched(@CurrentUser Viewer viewer,
                                                                    @RequestParam(required = false) MediaType type,
                                                                    Pageable pageable) {
        return ResponseEntity.ok(watchedMediaService.getWatchedMedia(viewer, type, pageable));
    }


    @DeleteMapping
    public ResponseEntity<Void> removeWatched(@CurrentUser Viewer viewer, @RequestParam Long mediaId,
                                              @RequestParam MediaType mediaType) {
        watchedMediaService.removeWatched(viewer, mediaId, mediaType);
        return ResponseEntity.noContent().build();
    }
}
